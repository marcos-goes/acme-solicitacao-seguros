package org.mgoes.acme.orders.business.payments;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentsListener {

    @RabbitListener(queues = "payments_queue")
    public void listen(String in) {
        log.info("Message: {}", in);
    }
}
