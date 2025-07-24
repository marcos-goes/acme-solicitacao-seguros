package org.mgoes.acme.orders.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mgoes.acme.orders.business.fraud.FraudClient;
import org.mgoes.acme.orders.model.HistoryItem;
import org.mgoes.acme.orders.model.Order;
import org.mgoes.acme.orders.repository.AssistanceRepository;
import org.mgoes.acme.orders.repository.CoverageRepository;
import org.mgoes.acme.orders.repository.HistoryItemRepository;
import org.mgoes.acme.orders.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final HistoryItemRepository historyItemRepository;
    private final AssistanceRepository assistanceRepository;
    private final CoverageRepository coverageRepository;

    private final FraudClient fraudClient;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Order createOrder(Order order){
        var initialStatus = "RECEIVED";
        var now = LocalDateTime.now();

        order.setId(UUID.randomUUID());
        order.setStatus(initialStatus);
        order.setCreatedAt(now);

        var historyItem = new HistoryItem();
        historyItem.setStatus(initialStatus);
        historyItem.setTimestamp(now);
        historyItem.setOrder(order);
        order.getHistory().add(historyItem);

        orderRepository.save(order);
        order.getHistory().forEach(historyItemRepository::save);
        order.getAssistances().forEach(assistanceRepository::save);
        order.getCoverages().forEach(coverageRepository::save);
        log.info("Order crated: {}", order.getId());

        fraudClient.executeAnalysis(order.getId(), order.getCustomerId());


        sendToTopic(order);

        log.info("Sent to topic");
        return order;
    }

    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public List<Order> findOrdersByCustomerId(UUID customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public List<Order> findOrders() {
        return orderRepository.findByOrderByCreatedAtDesc();
    }


    public void sendToTopic(Order order){
        rabbitTemplate.convertAndSend("orders_topic", "", order.getId().toString().getBytes());
    }
}
