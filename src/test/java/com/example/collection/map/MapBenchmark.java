package com.example.collection.map;

import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by tokuhirom on 9/17/16.
 */
public class MapBenchmark {

    @Test
    public void test() {
        HashMap<String, Object> hashMap = new HashMap<>();
        TreeMap<String, Object> treeMap = new TreeMap<>();
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();

        Consumer<Map<String, Object>> showItems = map -> {
            Arrays.asList("apple", "orange", "banana")
                    .stream()
                    .forEach(it -> map.put(it, it));

            System.out.println(map.entrySet().stream()
                    .map(it -> it.getKey().toString())
                    .collect(Collectors.joining(",")));
        };

        showItems.accept(hashMap);
        showItems.accept(treeMap);
        showItems.accept(linkedHashMap);
    }

}
