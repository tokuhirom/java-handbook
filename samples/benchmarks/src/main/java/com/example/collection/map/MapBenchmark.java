package com.example.collection.map;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MapBenchmark {
    public static void main(String[] args) {
        MapBenchmark mapBenchmark = new MapBenchmark();
        mapBenchmark.test();
    }

    public void test() {
        HashMap<String, Object> hashMap = new HashMap<>();
        TreeMap<String, Object> treeMap = new TreeMap<>();
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();

        Consumer<Map<String, Object>> showItems = map -> {
            Arrays.asList("apple", "orange", "banana")
                    .forEach(it -> map.put(it, it));

            System.out.println(map.entrySet().stream()
                    .map(Map.Entry::getKey)
                    .collect(Collectors.joining(",")));
        };

        showItems.accept(hashMap);
        showItems.accept(treeMap);
        showItems.accept(linkedHashMap);
    }

}
