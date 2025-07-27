package org.mgoes.acme.orders.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mgoes.acme.orders.model.HistoryItem;
import org.mgoes.acme.orders.model.IllegalOrderStateTransitionException;
import org.mgoes.acme.orders.model.Order;
import org.mgoes.acme.orders.model.OrderState;
import org.mgoes.acme.orders.repository.AssistanceRepository;
import org.mgoes.acme.orders.repository.CoverageRepository;
import org.mgoes.acme.orders.repository.HistoryItemRepository;
import org.mgoes.acme.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;

import javax.swing.plaf.nimbus.State;
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
    private final HistoryItemRepository historyItemRepository;
    private final OrderLifeCycleMediator mediator;

    public Order createOrder(Order order){
        var now = LocalDateTime.now();
        var id = UUID.randomUUID().toString();

        order.setId(id);
        order.setInitialState();
        order.setCreatedAt(now);

        saveOrder(order, MediatorEvent.CREATE_ORDER);
        mediator.notify(MediatorEvent.CREATE_ORDER, order.getId(), this);
        return order;
    }

    @Transactional
    public void saveOrder(Order order, MediatorEvent event){
        orderRepository.save(order);
        order.getAssistances().forEach(assistanceRepository::save);
        order.getCoverages().forEach(coverageRepository::save);
//        orderRepository.flush();
//        mediator.notify(event, order.getId(), this);
    }

//    @Transactional
    public void saveOrder(String orderId, OrderState nextState, MediatorEvent event){
        var order = getById(orderId);
        order.setState(nextState);
        saveOrder(order, null);
        mediator.notify(event, order.getId(), this);
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public Order getById(String id) {
        return orderRepository.getOne(id);
    }

    public List<Order> findOrdersByCustomerId(String customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    public List<Order> findOrders() {
        return orderRepository.findByOrderByCreatedAtDesc();
    }

    public boolean cancelOrder(String id) {
        var optOrder = orderRepository.findById(id);

        if(optOrder.isEmpty())
            return false;

        var order = optOrder.get();

        try {
            order.setState(OrderState.CANCELED);
        } catch (IllegalOrderStateTransitionException ex) {
            return false;
        }

        saveOrder(order, MediatorEvent.CANCEL_ORDER);
        mediator.notify(MediatorEvent.CANCEL_ORDER, order.getId(), this);
        return true;
    }

    @Transactional
    public void appendHistoryItem(String id, String event){
        var order = getById(id);
        var now = LocalDateTime.now();

        var historyItem = new HistoryItem();
        historyItem.setStatus(order.getStatus());
        historyItem.setAdditionalInfo(event);
        historyItem.setTimestamp(now);
        historyItem.setIdOrder(id);
        historyItemRepository.save(historyItem);
    }

}
