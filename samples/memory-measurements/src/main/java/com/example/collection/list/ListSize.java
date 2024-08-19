package com.example.collection.list;

import com.google.common.collect.ImmutableList;
import org.github.jamm.MemoryMeter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ListSize {
    public static void main(String[] args) {
        new ListSize().test();
    }

    public void test() {
        Stream.<List<Integer>>of(
                new ArrayList<>(),
                new LinkedList<>())
                .forEach(array -> {
                    IntStream.rangeClosed(0, 1_000_000)
                            .forEach(array::add);
                    calcAndPrintSize(array);
                });

        ImmutableList.Builder<Integer> builder = ImmutableList.builder();
        IntStream.rangeClosed(0, 1_000_000)
                .forEach(builder::add);
        calcAndPrintSize(builder.build());
    }

    private void calcAndPrintSize(Object o) {
        MemoryMeter memoryMeter = MemoryMeter.builder().build();
        long objectSize = memoryMeter.measure(o);
        long objectSizeDeep = memoryMeter.measureDeep(o);
        System.out.printf("%-30s: %-10s %-10s\n",
                o.getClass().getSimpleName(),
                NumberFormat.getInstance().format(objectSize),
                NumberFormat.getInstance().format(objectSizeDeep));
    }
}
