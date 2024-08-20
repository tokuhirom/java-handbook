package com.example.collection;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ListsTest {
    @Test
    public void test() {
        System.out.println(List.of());
        System.out.println(List.of("hello", "world"));

        var list = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        System.out.println(list);
    }
}
