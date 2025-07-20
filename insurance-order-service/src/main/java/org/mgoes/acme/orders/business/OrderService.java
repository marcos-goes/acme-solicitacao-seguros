package org.mgoes.acme.orders.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mgoes.acme.orders.mapper.OrderMapper;
import org.mgoes.acme.orders.model.HistoryItem;
import org.mgoes.acme.orders.model.Order;
import org.mgoes.acme.orders.openapi.api.OrdersApiDelegate;

import org.mgoes.acme.orders.openapi.model.OrderCreate;
import org.mgoes.acme.orders.openapi.model.OrderCreateResponse;
import org.mgoes.acme.orders.repository.AssistanceRepository;
import org.mgoes.acme.orders.repository.CoverageRepository;
import org.mgoes.acme.orders.repository.HistoryItemRepository;
import org.mgoes.acme.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final HistoryItemRepository historyItemRepository;
    private final AssistanceRepository assistanceRepository;
    private final CoverageRepository coverageRepository;

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

        return order;
    }

    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public List<Order> findOrderByCustomerId(UUID customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}
