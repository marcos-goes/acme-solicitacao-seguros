package org.mgoes.acme.orders.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.mgoes.acme.orders.mapper.OrderMapper;
import org.mgoes.acme.orders.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TopicNotificator {

    private final String ordersTopicName;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public TopicNotificator(@Value("${fraud.url}") String ordersTopicName, RabbitTemplate rabbitTemplate, ObjectMapper objectMapper){
        this.ordersTopicName = ordersTopicName;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Async
    public void sendToTopic(Order order) {
        var message = OrderMapper.INSTANCE.toApiOrder(order);
        try {
            var jsonContent = objectMapper.writeValueAsString(message);
            rabbitTemplate.convertAndSend(ordersTopicName, "", jsonContent.getBytes());
        } catch (JsonProcessingException ex){
            log.error("Error sending notification to topic.", ex);
        }
    }
}
