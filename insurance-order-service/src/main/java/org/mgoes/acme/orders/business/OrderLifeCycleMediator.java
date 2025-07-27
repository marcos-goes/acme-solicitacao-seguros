package org.mgoes.acme.orders.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mgoes.acme.orders.business.fraud.FraudService;
import org.mgoes.acme.orders.model.HistoryItem;
import org.mgoes.acme.orders.model.Order;
import org.mgoes.acme.orders.model.OrderState;
import org.mgoes.acme.orders.repository.HistoryItemRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static org.mgoes.acme.orders.business.MediatorEvent.PENDING_EXTERNAL_ACTIONS;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderLifeCycleMediator {

    private final OrderService orderService;
    private final TopicNotificator topicNotificator;
    private final HistoryItemRepository historyItemRepository;
    private final FraudService fraudService;
    private final ApproveOrderService approveOrderService;

    @Async
    public void notify(MediatorEvent event, String orderId){

        log.info("Starting actions related to event {} in Order id = {}", event.name(), orderId);

        var order = orderService.getOrderById(orderId).get();
        appendHistoryItem(event, order);
        var updatedOrder = orderService.getOrderById(orderId).get();
        topicNotificator.sendToTopic(updatedOrder);

        switch(event) {
            case CREATE_ORDER -> fraudService.executeRiskAnalysis(orderId, this);
            case PAYMENT_CONFIRMED, INSURANCE_SUBSCRITION_CONFIRMED -> approveOrderService.approve(orderId, this);
            case FRAUD_ANALISYS_ACCEPTED -> {
                updatedOrder.setState(OrderState.PENDING);
                orderService.saveOrder(updatedOrder);
                this.notify(PENDING_EXTERNAL_ACTIONS, updatedOrder.getId());
            }
        }

        log.info("Finishing actions related to event [{}] in Order {}", event.name(), orderId);
    }

    @Transactional
    public void appendHistoryItem(MediatorEvent event, Order order){
        var now = LocalDateTime.now();

        var historyItem = new HistoryItem();
        historyItem.setStatus(order.getStatus());
        historyItem.setAdditionalInfo(event.name());
        historyItem.setTimestamp(now);
        historyItem.setIdOrder(order.getId());
        historyItemRepository.save(historyItem);
    }
}
