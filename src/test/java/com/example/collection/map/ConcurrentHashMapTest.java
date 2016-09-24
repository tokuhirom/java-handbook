package com.example.collection.map;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {
    @Test
    public void test() {
        ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.computeIfAbsent("hello",
                it -> it + "!!!");
    }
}
