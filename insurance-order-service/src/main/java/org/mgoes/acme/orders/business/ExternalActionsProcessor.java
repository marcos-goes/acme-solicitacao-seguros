package org.mgoes.acme.orders.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

public interface ExternalActionsProcessor {

    void listen(String message) throws JsonProcessingException;

    default void process(String message, ObjectMapper objectMapper, MediatorEvent event, OrderService orderService, Logger log) throws JsonProcessingException {
        log.info("Incomming message {}: {}", event, message);
        var objMessage = objectMapper.readValue(message, IncomingMessage.class);
        var id = objMessage.getOrderId();

        orderService.getOrderById(id).ifPresent(order -> {
            orderService.saveOrder(order, event);
        });
    }
}
