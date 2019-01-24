package com.example.amqp.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class Http2AmqpApplication {
    public static void main(String[] args) throws IOException, TimeoutException {
        SpringApplication.run(Http2AmqpApplication.class, args);
    }
}
