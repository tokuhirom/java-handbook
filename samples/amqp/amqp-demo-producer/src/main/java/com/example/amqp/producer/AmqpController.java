package com.example.amqp.producer;

import com.example.amqp.model.DelayedRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
public class AmqpController {
    private final AmqpTemplate amqpTemplate;
    private final String queueName;

    @Autowired
    public AmqpController(
            AmqpTemplate amqpTemplate,
            AmqpAdmin amqpAdmin,
            @Value("${http2amqp.queue:http2amqp}") String queueName) {
        this.amqpTemplate = amqpTemplate;
        this.queueName = queueName;

        // キューを定義する。キューはすでに存在している場合 durable 等の
        // 設定は変更されない。
        log.info("Registering queue: {}", queueName);
        Queue queue = new Queue(queueName,
                // durable を true にすると永続化され、再起動しても消えない。
                true,
                // exclusive を true にすると、定義したコネクション以外では利用不可。
                false,
                // autoDelete を true にすると自動的に消える
                false);
        amqpAdmin.declareQueue(queue);
    }

    @RequestMapping("/**")
    public ResponseEntity<String> foo(@RequestBody byte[] bytes, HttpServletRequest request) {
        log.info("Got message: {}", new String(bytes, StandardCharsets.UTF_8));
        String signature = request.getHeader("X-Signature");
        if (signature == null) {
            return ResponseEntity.badRequest().body("Missing X-Signature header");
        }

        // 送信するメッセージを定義する
        DelayedRequest delayedRequest = new DelayedRequest(
                request.getRemoteAddr(),
                signature,
                bytes);

        // 実際に送信する。オブジェクトは Jackson でシリアライズされるように AmqpProducerConfig で設定している。
        amqpTemplate.convertAndSend(
                // exchange に空文字列を指定するとデフォルト exchange が利用される。
                // デフォルト exchange ではキュー名で指定されているキューに素直に入る。
                "",
                queueName,
                delayedRequest);

        return ResponseEntity.ok("Enqueued\n");
    }
}
