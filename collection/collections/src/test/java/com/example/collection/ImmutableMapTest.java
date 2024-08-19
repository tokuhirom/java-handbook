package com.example.collection;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;

public class ImmutableMapTest {
    @Test
    public void foo() {
        ImmutableMap.builder()
                .put("foo", "bar")
                .put("hoge", "fuga")
                .build();
    }
}
