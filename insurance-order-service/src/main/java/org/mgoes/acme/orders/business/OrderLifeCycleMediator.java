package org.mgoes.acme.orders.business;

import lombok.RequiredArgsConstructor;
import org.mgoes.acme.orders.model.HistoryItem;
import org.mgoes.acme.orders.model.Order;
import org.mgoes.acme.orders.model.OrderState;
import org.mgoes.acme.orders.repository.HistoryItemRepository;
import org.mgoes.acme.orders.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class OrderLifeCycleMediator {

    private final OrderService orderService;
    private final TopicNotificator topicNotificator;
    private final OrderRepository repo;
    private final HistoryItemRepository historyItemRepository;

//    public void notify(MediatorEvent event, Order order){
////        switch(event) {
////            case CREATE_ORDER ->
////        }
//
////        var updatedOrder = appendHistoryItem(event, order);
//        topicNotificator.sendToTopic(order);
//    }

//    @Async
    public void notify(MediatorEvent event, String id){
//        switch(event) {
//            case CREATE_ORDER ->
//        }

//        var updatedOrder = appendHistoryItem(event, order);
//        var order = repo.getOne(id);
//        var order = orderService.getOrderById(id).get();

//        optOrder.if

        var order = orderService.getOrderById(id).get();

        appendHistoryItem(MediatorEvent.CREATE_ORDER, order);
//        topicNotificator.sendToTopic(order);
    }

    private void createOrder(Order order, MediatorEvent event){
        var now = LocalDateTime.now();

        orderService.saveOrder(order);
    }

    private void appendHistoryItem(MediatorEvent event, Order order){
        var now = LocalDateTime.now();
        var historyItem = new HistoryItem();
        historyItem.setStatus(order.getStatus());
        historyItem.setAdditionalInfo("Event: " + event.name());
        historyItem.setTimestamp(now);
        historyItem.setIdOrder(order.getId());
        historyItemRepository.save(historyItem);
//        order.getHistory().add(historyItem);
//        orderService.saveOrder(order);
//        repo.save(order);
    }


}
