package com.example.amqp.consumer;

import com.example.amqp.model.DelayedRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class Http2AmqpListener {
    // 実際にキューを処理するハンドラを定義。
    @RabbitListener(queues = "${http2amqp.queue:http2amqp}")
    public void processMessage(DelayedRequest content) {
        System.out.println(content);
        System.out.println(new String(content.getBody(), StandardCharsets.UTF_8));
    }
}
