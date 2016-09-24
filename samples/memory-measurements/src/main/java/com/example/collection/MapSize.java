package com.example.collection;

import com.google.common.collect.ImmutableMap;
import org.github.jamm.MemoryMeter;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// This test needs to run with -javaagent:path-to/jamm.jar
public class MapSize {
    public static void main(String[] args) {
        new MapSize().test();
    }

    public void test() {
        Stream.<Map<Integer, Integer>>of(new HashMap<>(),
                new LinkedHashMap<>(),
                new TreeMap<>())
                .forEach(map -> {
                    IntStream.rangeClosed(0, 1_000_000)
                            .forEach(i -> map.put(i, i));
                    calcAndPrintSize(map);
                });

        ImmutableMap.Builder<Integer, Integer> builder = ImmutableMap.builder();
        IntStream.rangeClosed(0, 1_000_000)
                .forEach(i -> builder.put(i, i));
        calcAndPrintSize(builder.build());
    }

    private void calcAndPrintSize(Object o) {
        MemoryMeter memoryMeter = new MemoryMeter();
        long objectSize = memoryMeter.measure(o);
        long objectSizeDeep = memoryMeter.measureDeep(o);
        System.out.printf("%-15s: %-10s %-10s\n",
                o.getClass().getSimpleName(),
                NumberFormat.getInstance().format(objectSize),
                NumberFormat.getInstance().format(objectSizeDeep));
    }

}
