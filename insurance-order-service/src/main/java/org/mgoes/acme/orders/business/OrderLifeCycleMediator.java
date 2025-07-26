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

        switch(event) {
            case CREATE_ORDER -> fraudService.executeRiskAnalysis(orderId, this);
        }

//        var updatedOrder = appendHistoryItem(event, order);
//        var order = repo.getOne(id);
//        var order = orderService.getOrderById(id).get();

//        optOrder.if

        var order = orderService.getOrderById(orderId).get();
        appendHistoryItem(event, order);
        var updatedOrder = orderService.getOrderById(orderId).get();
        topicNotificator.sendToTopic(updatedOrder);
        log.info("Finishing actions related to event [{}] in Order id = {}", event.name(), orderId);
    }

    @Transactional
    private void appendHistoryItem(MediatorEvent event, Order order){
        var now = LocalDateTime.now();
        var historyItem = new HistoryItem();
        historyItem.setStatus(order.getStatus());
        historyItem.setAdditionalInfo("Event: " + event.name());
        historyItem.setTimestamp(now);
        historyItem.setIdOrder(order.getId());
        historyItemRepository.save(historyItem);
    }


}
