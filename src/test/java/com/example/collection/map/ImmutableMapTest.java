package com.example.collection.map;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

/**
 * Created by tokuhirom on 9/17/16.
 */
public class ImmutableMapTest {
    @Test
    public void foo() {
        ImmutableMap.builder()
                .put("foo", "bar")
                .put("hoge", "fuga")
                .build();

    }
}
