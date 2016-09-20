package com.example.collection.map;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

public class ImmutableMapTest {
    @Test
    public void foo() {
        ImmutableMap.builder()
                .put("foo", "bar")
                .put("hoge", "fuga")
                .build();
    }
}
