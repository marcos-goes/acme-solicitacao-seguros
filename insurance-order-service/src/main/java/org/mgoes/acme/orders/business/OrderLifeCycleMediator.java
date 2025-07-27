package org.mgoes.acme.orders.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mgoes.acme.orders.business.fraud.FraudService;
import org.mgoes.acme.orders.model.HistoryItem;
import org.mgoes.acme.orders.model.Order;
import org.mgoes.acme.orders.repository.HistoryItemRepository;
import org.mgoes.acme.orders.repository.OrderRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderLifeCycleMediator {

    private final OrderService orderService;
    private final TopicNotificator topicNotificator;
    private final HistoryItemRepository historyItemRepository;
    private final FraudService fraudService;

    @Async
    public void notify(MediatorEvent event, String orderId){

        log.info("Starting actions related to event {} in Order id = {}", event.name(), orderId);

        var order = orderService.getOrderById(orderId).get();
        appendHistoryItem(event, order);
        var updatedOrder = orderService.getOrderById(orderId).get();
        topicNotificator.sendToTopic(updatedOrder);

        switch(event) {
            case CREATE_ORDER -> fraudService.executeRiskAnalysis(orderId, this);
        }

        log.info("Finishing actions related to event [{}] in Order id = {}", event.name(), orderId);
    }




}
