package com.example.amqp.consumer;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpReceiverConfig {
    // 受信側。body を jackson でデシリアライズする設定。
    @Autowired
    public void rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactory factory) {
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
    }
}
