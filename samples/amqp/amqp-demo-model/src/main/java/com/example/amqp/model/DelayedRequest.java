package com.example.amqp.model;

import lombok.Value;

@Value
public class DelayedRequest {
    private final String remoteAddress;
    private final String signature;
    private final byte[] body;
}
