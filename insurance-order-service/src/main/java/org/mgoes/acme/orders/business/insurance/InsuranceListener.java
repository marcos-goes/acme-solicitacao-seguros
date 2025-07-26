package org.mgoes.acme.orders.business.insurance;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class InsuranceListener {

//    private final String queueName;
//
//    public InsuranceListener(@Value("${messaging.queue.insurance}") String queueName) {
//        this.queueName = queueName;
//    }

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "${messaging.queue.insurance}")
    public void listen(String in) {

        objectMapper.read
        log.info("Message: {}", in);
    }
}