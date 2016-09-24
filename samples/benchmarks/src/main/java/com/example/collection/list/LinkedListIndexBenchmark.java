package com.example.collection.list;

import com.example.collection.Benchmark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class LinkedListIndexBenchmark {
    public static void main(String[] args) throws Exception {
        LinkedListIndexBenchmark benchmark = new LinkedListIndexBenchmark();
        benchmark.indexBench();
        benchmark.streamTest();
    }

    public void indexBench() throws Exception {
        for (Integer x : Arrays.asList(1000, 10_000)) {
            System.out.printf("--- %d ---\n", x);
            Benchmark benchmark = new Benchmark(new IndexBenchmark(x));
            benchmark.warmup(1000);
            benchmark.runByTime(1).timethese().cmpthese();
        }
    }

    public class IndexBenchmark {
        private final ArrayList<Integer> arrayList;
        private final LinkedList<Integer> linkedList;
        private final int size;

        public IndexBenchmark(int size) {
            this.size = size;
            arrayList = new ArrayList<>(size);
            linkedList = new LinkedList<>();

            IntStream.rangeClosed(0, size)
                    .forEach(arrayList::add);
            IntStream.rangeClosed(0, size)
                    .forEach(linkedList::add);
        }

        @Benchmark.Bench
        public void arrayList() {
            for (int i = 0; i < size; ++i) {
                arrayList.get(i);
            }
        }

        @Benchmark.Bench
        public void linkedList() {
            for (int i = 0; i < size; ++i) {
                linkedList.get(i);
            }
        }
    }

    public void streamTest() throws Exception {
        for (Integer x : Arrays.asList(1000, 10_000)) {
            System.out.printf("--- %d ---\n", x);
            Benchmark benchmark = new Benchmark(new SumBenchmark(x));
            benchmark.warmup(10_000);
            benchmark.run(10_000).timethese().cmpthese();
        }
    }

    public class SumBenchmark {
        private final ArrayList<Integer> arrayList;
        private final LinkedList<Integer> linkedList;
        private final int size;

        public SumBenchmark(int size) {
            this.size = size;
            arrayList = new ArrayList<>(size);
            linkedList = new LinkedList<>();

            IntStream.rangeClosed(0, size)
                    .forEach(arrayList::add);
            IntStream.rangeClosed(0, size)
                    .forEach(linkedList::add);
        }

        @Benchmark.Bench
        public void arrayList() {
            Integer n = 0;
            for (Integer integer : arrayList) {
                n += integer;
            }
        }

        @Benchmark.Bench
        public void linkedList() {
            Integer n = 0;
            for (Integer integer : linkedList) {
                n += integer;
            }
        }
    }

}
