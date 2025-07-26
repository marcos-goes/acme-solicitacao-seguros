package org.mgoes.acme.orders.business;

import org.mgoes.acme.orders.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TopicNotificator {

    private final String ordersTopicName;
    private final RabbitTemplate rabbitTemplate;

    public TopicNotificator(@Value("${fraud.url}") String ordersTopicName, RabbitTemplate rabbitTemplate){
        this.ordersTopicName = ordersTopicName;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Async
    public void sendToTopic(Order order){
        rabbitTemplate.convertAndSend(ordersTopicName, "", order.getId().toString().getBytes());
    }
}
