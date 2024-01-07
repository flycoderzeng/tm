package com.tm.worker.message;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AutoTestFanoutRabbitConfig {

    public static final String AUTO_TEST_FANOUT_EXCHANGE = "AutoTestFanoutExchange";

    @Bean
    public Queue autoTestFanoutQueue() {
        return new Queue("");
    }

    @Bean
    public FanoutExchange autoTestFanoutExchange() {
        return new FanoutExchange(AUTO_TEST_FANOUT_EXCHANGE);
    }

    @Bean
    public Binding bindingFanoutExchange() {
        return BindingBuilder.bind(autoTestFanoutQueue()).to(autoTestFanoutExchange());
    }
}
