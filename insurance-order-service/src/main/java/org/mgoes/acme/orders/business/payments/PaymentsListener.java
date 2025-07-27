package org.mgoes.acme.orders.business.payments;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mgoes.acme.orders.business.ExternalActionsProcessor;
import org.mgoes.acme.orders.business.OrderLifeCycleMediator;
import org.mgoes.acme.orders.business.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static org.mgoes.acme.orders.business.MediatorEvent.PAYMENT_CONFIRMED;

@RequiredArgsConstructor
@Slf4j
@Component
public class PaymentsListener implements ExternalActionsProcessor {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final OrderLifeCycleMediator mediator;

    @Override
    @RabbitListener(queues = "${messaging.queue.payments}")
    public void listen(String message) throws JsonProcessingException {
        this.process(message, objectMapper, PAYMENT_CONFIRMED, orderService, mediator, log);
    }
}
