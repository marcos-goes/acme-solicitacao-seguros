package org.mgoes.acme.orders;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    private static final String ORDERS_TOPIC = "orders_topic";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(ORDERS_TOPIC);
    }
}
