package com.example;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright © 2014 Tokuhiro Matsuno, http://64p.org/
 * <p>
 * This is free software; you can redistribute it and/or modify it under the
 * same terms as the Perl 5 programming language system itself.
 */
public class Benchmark {

    private final Map<Long, Score> emptyLoopCache = new HashMap<>();
    private boolean debug = false;
    private final Object suite;

    final Method[] methods;

    public Benchmark(Object suite) {
        this.suite = suite;
        Method[] allMethods = suite.getClass().getMethods();
        ArrayList<Method> methodsList = new ArrayList<>();
        for (Method method : allMethods) {
            if (method.getName().startsWith("bench") || method.isAnnotationPresent(Bench.class)) {
                methodsList.add(method);
            }
        }
        this.methods = methodsList.toArray(new Method[methodsList.size()]);
    }

    public static void main(String[] args) throws Exception {
        new CLI().run(args);
    }

    static class CLI {

        public void run(String[] args) throws Exception {
            if (args.length == 0) {
                help();
                return;
            }

            File f = new File(".");
            URL[] cp = {f.toURI().toURL()};
            try (URLClassLoader classLoader = new URLClassLoader(cp,
                    ClassLoader.getSystemClassLoader())) {
                Class<?> targetClass = classLoader.loadClass(args[0]);
                Object suite = targetClass.newInstance();
                Benchmark benchmark = new Benchmark(suite);
                benchmark.runByTime(1).timethese().cmpthese();
            }
        }

        public void help() {
            System.out.println("Usage: java -jar nanobench.jar BenchmarkClass");
        }
    }

    public Benchmark enableDebugging() {
        debug = true;
        return this;
    }

    /**
     * Run the code for warming up.
     *
     * @param ntimes
     * @return
     * @throws Exception
     */
    public Benchmark warmup(int ntimes) throws Exception {
        System.out.println("Warm up: " + ntimes + "\n");

        for (Method method : methods) {
            for (int i = 0; i < ntimes; ++i) {
                method.invoke(this.suite);
            }
        }

        DEBUG("Warm up done.");
        return this;
    }

    private Score measureEmptyLoop(long ntimes) throws Exception {
        if (!emptyLoopCache.containsKey(ntimes)) {
            Score empty = this.runloop(ntimes,
                    Benchmark.class.getMethod("emptyMethod"));
            emptyLoopCache.put(ntimes, empty);
            return empty;
        } else {
            DEBUG("Hit empty loop cache.");
            return emptyLoopCache.get(ntimes);
        }
    }

    public static void emptyMethod() {
    }

    /**
     * Run all scenarios.
     *
     * @param ntimes
     * @return
     * @throws Exception
     */
    public Result run(int ntimes) throws Exception {
        List<ScenarioResult> results = new ArrayList<>();
        for (Method method : this.methods) {
            Score t = timeit(ntimes, method);
            results.add(new ScenarioResult(method.getName(), t));
        }

        return new Result(results);
    }

    public Result runByTime(double d) throws Exception {
        List<ScenarioResult> results = new ArrayList<>();
        for (Method method : methods) {
            DEBUG("Running " + method.getName());
            Score t = countit(d, method);
            results.add(new ScenarioResult(method.getName(), t));
        }

        return new Result(results);
    }

    /**
     * Clear all cached times.
     */
    public void clearAllCache() {
        emptyLoopCache.clear();
    }

    /**
     * Clear the cached time for COUNT rounds of the null loop.
     *
     * @param ntimes
     */
    public void clearCache(long ntimes) {
        emptyLoopCache.remove(ntimes);
    }

    /**
     * <i>ntimes</i> is the number of times to run the loop, and <i>code</i> is
     * the code to run. <i>code</i> may be either a code reference or a string
     * to be eval'd; either way it will be run in the caller's package.
     *
     * @param ntimes
     * @param method
     * @return elapsed time in nanoseconds
     * @throws Exception
     */
    public Score timeit(long ntimes, Method method) throws Exception {
        Score empty = this.measureEmptyLoop(ntimes);
        Score score = this.runloop(ntimes, method);

        return score.diff(empty);
    }

    public Score countit(double tmax, String methodName) throws Exception {
        return this.countit(tmax, this.suite.getClass().getMethod(methodName));
    }

    public Score countit(double tmax, Method method) throws Exception {
        if (tmax < 0.1) {
            throw new IllegalArgumentException(
                    "timelimit cannot be less than '0.1'.");
        }

        // First find the minimum $n that gives a significant timing.
        int zeros = 0;
        long n = 1;
        double tc = 0.0;
        for (; ; n *= 2) {
            DEBUG("Finding minimum n: " + n);

            Score td = timeit(n, method);
            tc = (double) td.cputime / 1_000_000_000.0;
            DEBUG(String.format("TC: %.8f", tc));
            if (tc <= 0.01 && n > 1024) {
                if (++zeros > 16) {
                    throw new RuntimeException(
                            "Timing is consistently zero in estimation loop, cannot benchmark. N="
                                    + n);
                }
            } else {
                zeros = 0;
            }
            if (tc > 0.1) {
                break;
            }
            if (n < 0) {
                throw new RuntimeException("Overflow?");
            }
        }

        long nmin = n;
        // Get $n high enough that we can guess the final $n with some accuracy.
        double tpra = 0.1 * tmax; // Target/time practice.
        while (tc < tpra) {
            /*
             * # The 5% fudge is to keep us from iterating again all # that
			 * often (this speeds overall responsiveness when $tmax is big # and
			 * we guess a little low). This does not noticeably affect #
			 * accuracy since we're not counting these times.
			 */
            n = (int) (tpra * 1.05 * n / tc); // Linear approximation.
            Score td = timeit(n, method);
            double new_tc = td.cputime / 1_000_000_000.0;
            // Make sure we are making progress.
            tc = new_tc > 1.2 * tc ? new_tc : 1.2 * tc;
        }

        // Now, do the 'for real' timing(s), repeating until we exceed
        // the max.
        Score result = new Score(0, 0, 0, 0, 0);

        // The 5% fudge is because $n is often a few % low even for routines
        // with stable times and avoiding extra timeit()s is nice for
        // accuracy's sake.
        n = (int) (n * (1.05 * tmax / tc));
        zeros = 0;
        while (true) {
            Score td = timeit(n, method);
            result = result.add(td);
            if ((result.cputime / 1_000_000_000.0) >= tmax) {
                break;
            }
            if (result.cputime == 0) {
                if (++zeros > 16) {

                    throw new RuntimeException(
                            "Timing is consistently zero in estimation loop, cannot benchmark. N="
                                    + n);
                }
            } else {
                zeros = 0;
            }
            if (result.cputime < 10_000_000) {
                result.cputime = 10_000_000;
            }
            double r = tmax / (result.cputime / 1_000_000_000.0) - 1; // Linear
            // approximation.
            n = (int) (r * result.iters);
            if (n < nmin) {
                n = nmin;
            }
        }
        return result;
    }

    private void DEBUG(String message) {
        if (debug) {
            System.out.println("[DEBUG] " + message);
        }
    }

    private Score runloop(long ntimes, Method method) throws Exception {
        System.gc();
        System.runFinalization();

        long real1 = System.nanoTime();
        long cputime1 = this.getCpuTime();
        long usertime1 = this.getUserTime();
        long systemtime1 = this.getSystemTime();
        for (int i = 0; i < ntimes; ++i) {
            method.invoke(this.suite);
        }
        long real2 = System.nanoTime();
        long cputime2 = this.getCpuTime();
        long usertime2 = this.getUserTime();
        long systemtime2 = this.getSystemTime();

        Score score = new Score(//
                real2 - real1, //
                cputime2 - cputime1, //
                usertime2 - usertime1, //
                systemtime2 - systemtime1, //
                ntimes);

        System.gc();
        System.runFinalization();

        return score;
    }

    public static class Score {

        public long cputime;
        public final long usertime;
        public final long systemtime;
        public final long iters;
        public final long real;

        public Score(long real, long cputime, long usertime, long systemtime,
                     long iters) {
            this.real = real;
            this.cputime = cputime;
            this.usertime = usertime;
            this.systemtime = systemtime;
            this.iters = iters;
        }

        public Score add(Score other) {
            return new Score( //
                    this.real + other.real, //
                    this.cputime + other.cputime, //
                    this.usertime + other.usertime, //
                    this.systemtime + other.systemtime, //
                    this.iters + other.iters);
        }

        public Score diff(Score other) {
            return new Score( //
                    Math.max(this.real - other.real, 0), //
                    Math.max(this.cputime - other.cputime, 0), //
                    Math.max(this.usertime - other.usertime, 0), //
                    Math.max(this.systemtime - other.systemtime, 0), //
                    this.iters);
        }

        public String format() {
            // timestr
            long n = iters;
            long elapsed = usertime + systemtime;
            StringBuilder builder = new StringBuilder();
            builder.append(String.format(
                    "%2d wallclock secs (%5.2f usr + %5.2f sys = %5.2f CPU)",
                    (long) (real / 1_000_000_000.0),
                    (double) usertime / 1_000_000_000.0,
                    (double) systemtime / 1_000_000_000.0,
                    (double) cputime / 1_000_000_000.0));
            if (elapsed > 0) {
                builder.append(String.format(" @ %5.2f/s (n=%d)", //
                        (double) n / (elapsed / 1_000_000_000.0), //
                        n));
            }
            return builder.toString();
        }

        public double rate() {
            long elapsed = usertime + systemtime;
            return (double) iters / (elapsed / 1_000_000_000.0);
        }

        public String formatRate() {
            double rate = rate();
            String format = rate >= 100 ? "%.0f" : rate >= 10 ? "%.1f"
                    : rate >= 1 ? "%.2f" : rate >= 0.1 ? "%.3f" : "%.2f";
            return String.format(format + "/s", rate);
        }
    }

    /**
     * Get CPU time in nanoseconds.
     */
    public long getCpuTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ? bean
                .getCurrentThreadCpuTime() : 0L;
    }

    /**
     * Get user time in nanosecond
     *
     * @return
     */
    long getUserTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ? bean
                .getCurrentThreadUserTime() : 0L;
    }

    /**
     * Get system time in nanoseconds.
     */
    long getSystemTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ? (bean
                .getCurrentThreadCpuTime() - bean.getCurrentThreadUserTime())
                : 0L;
    }

    public static class Result {

        private final List<ScenarioResult> results;

        public Result(List<ScenarioResult> results) {
            this.results = results;
        }

        public Result timethese() {
            System.out.println("\nScore:\n");

            for (ScenarioResult result : results) {
                System.out.println(result.title + ": " + result.score.format());
            }
            return this;
        }

        /**
         * [ '', 'Rate', 'b', 'a' ], [ 'b', '2885232/s', '--', '-59%' ], [ 'a',
         * '7099126/s', '146%', '--' ],
         *
         * @return
         */
        public Result cmpthese() {
            System.out.println("\nComparison chart:\n");

            List<List<String>> rows = this.createComparisionTable();
            System.out.print(this.renderTable(rows));
            return this;
        }

        public List<List<String>> createComparisionTable() {
            List<List<String>> rows = new ArrayList<>();
            List<String> headerRow = new ArrayList<>();
            headerRow.add("");
            headerRow.add("Rate");
            for (ScenarioResult result : results) {
                headerRow.add(result.title);
            }
            rows.add(headerRow);

            for (ScenarioResult result : results) {
                List<String> row = new ArrayList<>();
                row.add(result.title);
                row.add(result.score.formatRate());

                for (ScenarioResult col : results) {
                    if (col == result) {
                        row.add("--");
                    } else {
                        row.add(String.format("%.0f%%",
                                100 * result.score.rate() / col.score.rate()
                                        - 100));
                    }
                }
                rows.add(row);
            }
            return rows;
        }

        public String renderTable(List<List<String>> rows) {
            StringBuilder buffer = new StringBuilder();
            List<Integer> colSizes = new ArrayList<>(rows.get(0).size());
            for (int x = 0; x < rows.get(0).size(); ++x) {
                colSizes.add(1); // fill initial values.
            }
            for (int x = 0; x < rows.get(0).size(); ++x) {
                for (int y = 0; y < rows.size(); ++y) {
                    List<String> row = rows.get(y);
                    String col = row.get(x);
                    colSizes.set(x, Math.max(colSizes.get(x), col.length()));
                    // Integer currentX = colSizes.get(x);
                }
            }

            for (int y = 0; y < rows.size(); ++y) {
                List<String> row = rows.get(y);
                for (int x = 0; x < row.size(); ++x) {
                    buffer.append(String.format("  %" + colSizes.get(x) + "s",
                            row.get(x)));
                }
                buffer.append("\n");
            }
            return buffer.toString();
        }
    }

    public static class ScenarioResult {

        public final String title;
        // in nanosec.
        public final Score score;

        @Override
        public String toString() {
            return "Result [title=" + title + ", score=" + score + "]";
        }

        public ScenarioResult(String title, Score score) {
            this.title = title;
            this.score = score;
        }
    }

    /**
     * The {@code Bench} annotation tells nanobench that the {@code public} method
     * to which it is attached can be run as a benchmark target.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Bench {
    }
}