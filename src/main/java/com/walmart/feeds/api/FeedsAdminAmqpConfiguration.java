package com.walmart.feeds.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeedsAdminAmqpConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedsAdminAmqpConfiguration.class);

    @Value("${amqp.exchange}")
    private String exchangeName;

    @Value("${amqp.generator-routing-key}")
    private String routingKey;

    @Value("${amqp.generator-queue-name}")
    private String queueName;


    @Bean
    public Queue generatorQueue() {
        return new Queue(queueName);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding binding(@Qualifier("exchange") DirectExchange exchange,
                           @Qualifier("generatorQueue") Queue queue) {
        LOGGER.info("Binding {} to exchange {}", exchange, queue);
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }
}
