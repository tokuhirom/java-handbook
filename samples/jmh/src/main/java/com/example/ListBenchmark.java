package com.example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.stream.IntStream;

public class ListBenchmark {
    @Benchmark
    @BenchmarkMode(Mode.All)
    public int streamSum() throws URISyntaxException {
        return IntStream.range(0, 1000)
                .sum();
    }

    @Benchmark
    @BenchmarkMode(Mode.All)
    public int forSum() throws MalformedURLException {
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i;
        }
        return sum;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ListBenchmark.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
