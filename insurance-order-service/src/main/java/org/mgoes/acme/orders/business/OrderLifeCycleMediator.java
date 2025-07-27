package org.mgoes.acme.orders.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mgoes.acme.orders.business.fraud.FraudService;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderLifeCycleMediator {

    private final TopicNotificator topicNotificator;
    private final FraudService fraudService;

    public void notify(MediatorEvent event, String orderId, OrderService orderService){

        log.info("Starting actions related to event {} in Order id = {}", event.name(), orderId);

        orderService.appendHistoryItem(orderId, event.name());
        var updatedOrder = orderService.getById(orderId);
        topicNotificator.sendToTopic(updatedOrder);

        switch(event) {
            case CREATE_ORDER -> fraudService.executeRiskAnalysis(orderId, orderService);
        }

        log.info("Finishing actions related to event [{}] in Order id = {}", event.name(), orderId);
    }




}
