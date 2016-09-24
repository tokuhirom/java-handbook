package com.example.collection.list;

import com.example.collection.Benchmark;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LinkedListBenchmark {
    public static void main(String[] args) throws Exception {
        LinkedListBenchmark benchmark = new LinkedListBenchmark();
        benchmark.addFirst();
        benchmark.addLast();
        benchmark.removeFirst();
    }

    public void addFirst() throws Exception {
        Benchmark benchmark = new Benchmark(new AddFirstListBenchmark());
        benchmark.runByTime(100).timethese().cmpthese();
    }

    public class AddFirstListBenchmark {
        @Benchmark.Bench
        public void arrayList() {
            List<Integer> l = new ArrayList<>();
            for (int i = 0; i < 1_000_000; ++i) {
                l.add(0, i);
            }
        }

        @Benchmark.Bench
        public void linkedList() {
            LinkedList<Integer> l = new LinkedList<>();
            for (int i = 0; i < 1_000_000; ++i) {
                l.addFirst(i);
            }
        }
    }

    public void addLast() throws Exception {
        Benchmark benchmark = new Benchmark(new AddLastBenchmark());
        benchmark.runByTime(100).timethese().cmpthese();
    }

    public class AddLastBenchmark {
        @Benchmark.Bench
        public void arrayList() {
            ArrayList<Integer> l = new ArrayList<>();
            for (int i = 0; i < 1_000_000; ++i) {
                l.add(l.size(), i);
            }
        }

        @Benchmark.Bench
        public void linkedList() {
            LinkedList<Integer> l = new LinkedList<>();
            for (int i = 0; i < 1_000_000; ++i) {
                l.addLast(i);
            }
        }
    }

    public void removeFirst() throws Exception {
        Benchmark benchmark = new Benchmark(new RemoveFirstBenchmark());
        benchmark.runByTime(100).timethese().cmpthese();
    }

    public class RemoveFirstBenchmark {
        List<Integer> ints = IntStream.rangeClosed(0, 1_000_000)
                .mapToObj(i -> i)
                .collect(Collectors.toList());

        @Benchmark.Bench
        public void arrayList() {
            ArrayList<Integer> l = new ArrayList<>(ints);
            while (!l.isEmpty()) {
                l.remove(0);
            }
        }

        @Benchmark.Bench
        public void linkedList() {
            LinkedList<Integer> l = new LinkedList<>(ints);
            while (!l.isEmpty()) {
                l.removeFirst();
            }
        }
    }

}
