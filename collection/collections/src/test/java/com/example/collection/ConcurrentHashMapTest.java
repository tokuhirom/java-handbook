package com.example.collection;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;

public class ConcurrentHashMapTest {
    @Test
    public void test() {
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.computeIfAbsent("hello",
                it -> it + "!!!");
    }
}
