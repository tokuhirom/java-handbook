package com.example.amqp.producer;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpProducerConfig {
    // rabbitTemplate は Jackson で JSON にシリアライズするように設定。
//    @Autowired
//    public void rabbitTemplateConfig(RabbitTemplate amqpTemplate) {
//        amqpTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//    }
}
