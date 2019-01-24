# spring-amqp

Spring には AMQP を利用するためのコンポーネントとして spring-amqp が存在しています。

AMQP は Advanced Message Queuing Protocol の略です。
メッセージオリエンテッドミドルウェアで使われている標準的なプロトコルです。

## rabbitmq をインストールする

rabbitmq を例にします。rabbitmq は AMQP の一実装です。

rabbitmq のインストールを OSX でするには、homebrew を使えば簡単です。
 
```shell
brew install rabbitmq
```

起動は、普通にオプションつけずに起動すれば良いです。

```shell
/usr/local/sbin/rabbitmq-server
```

起動したら http://localhost:15672/ で management console にアクセス可能です。
とても使いやすいコンソールです。

## モデルを定義する

まずは以下のように、メッセージ送受信に使うモデルを定義します。

```java
package com.example.amqp.model;

import lombok.Value;

@Value
public class DelayedRequest {
    private final String remoteAddress;
    private final String signature;
    private final byte[] body;
}
```

## メッセージを送信する

HTTP で来たメッセージを RabbitMQ に送信するようなコードを書いてみましょう。

AmqpAdmin を利用して Queue を定義します。
 
あとは AmqpTemplate を利用して、メッセージを送信すれば完了です。

```java
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
```

### シリアライザをカスタマイズする。

JSON でシリアライズしたほうが、他の言語でもアクセスしやすいので JSON にシリアライズするように設定したい場合、以下のように message converter を設定すれば良いです。

```java
package com.example.amqp.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpProducerConfig {
    // rabbitTemplate が Jackson で JSON にシリアライズするように設定。
    @Autowired
    public void rabbitTemplateConfig(RabbitTemplate amqpTemplate) {
        amqpTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }
}
```

## メッセージを受信して処理する

`@RabbitListener` というアノテーションがついたハンドラを定義するだけで OK です。

```java
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
```

### JSON を受信するリスナーの定義

JSON を受信するリスナーを設定するには以下のように設定します。

```java
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
```

## 設定について

rabbitmq への接続設定については

[RabbitProperties](https://github.com/spring-projects/spring-boot/blob/v1.4.1.RELEASE/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/amqp/RabbitProperties.java) をご覧ください。

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=admin
spring.rabbitmq.password=secret
```

## SEE ALSO

 * [rabbitmq](http://www.rabbitmq.com/)
 * [cloudamqp の rabbitmq tutorial](https://www.cloudamqp.com/blog/2015-05-18-part1-rabbitmq-for-beginners-what-is-rabbitmq.html)
   * exchange と routing の関係についてはこのドキュメントが一番わかりやすいです
 * [spring-boot + amqp document](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-messaging.html)
 