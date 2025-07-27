package org.mgoes.acme.orders.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mgoes.acme.orders.model.IllegalOrderStateTransitionException;
import org.mgoes.acme.orders.model.Order;
import org.mgoes.acme.orders.model.OrderState;
import org.mgoes.acme.orders.repository.AssistanceRepository;
import org.mgoes.acme.orders.repository.CoverageRepository;
import org.mgoes.acme.orders.repository.OrderRepository;
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
    private final AssistanceRepository assistanceRepository;
    private final CoverageRepository coverageRepository;

    public Order createOrder(Order order){
        var now = LocalDateTime.now();
        var id = UUID.randomUUID().toString();

        order.setId(id);
        order.setInitialState();
        order.setCreatedAt(now);

        saveOrder(order);
        return order;
    }

    @Transactional
    public void saveOrder(Order order){
        orderRepository.save(order);
        order.getAssistances().forEach(assistanceRepository::save);
        order.getCoverages().forEach(coverageRepository::save);
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public List<Order> findOrdersByCustomerId(String customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public List<Order> findOrders() {
        return orderRepository.findByOrderByCreatedAtDesc();
    }

    @Transactional
    public boolean cancelOrder(String id) {
        var optOrder = orderRepository.findById(id);

        if(optOrder.isEmpty())
            return false;

        var order = optOrder.get();

        try {
            order.setState(OrderState.CANCELED);
            order.setFinishedAt(LocalDateTime.now());
        } catch (IllegalOrderStateTransitionException ex) {
            return false;
        }

        orderRepository.save(order);
        return true;
    }

}
