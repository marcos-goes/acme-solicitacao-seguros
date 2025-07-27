package org.mgoes.acme.orders.business.insurance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mgoes.acme.orders.business.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static org.mgoes.acme.orders.business.MediatorEvent.INSURANCE_SUBSCRITION_CONFIRMED;

@RequiredArgsConstructor
@Slf4j
@Component
public class InsuranceListener implements ExternalActionsProcessor {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;

    @RabbitListener(queues = "${messaging.queue.insurance}")
    public void listen(String message) throws JsonProcessingException {
        this.process(message, objectMapper, INSURANCE_SUBSCRITION_CONFIRMED, orderService, log);
    }
}