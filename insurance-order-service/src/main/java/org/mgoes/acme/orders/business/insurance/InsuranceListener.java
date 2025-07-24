package org.mgoes.acme.orders.business.insurance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InsuranceListener {

    @RabbitListener(queues = "insurance_queue")
    public void listen(String in) {
        log.info("Message: {}", in);
    }
}