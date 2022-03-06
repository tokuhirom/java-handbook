# jmh - microbenchmark framework

jmh は microbenchmark framework です。

ref. http://openjdk.java.net/projects/code-tools/jmh/

`jmh-core` と `jmh-generator-anonprocess` を依存に入れます。

```groovy
dependencies {
  implementation 'org.openjdk.jmh:jmh-core:1.14.1'
  implementation 'org.openjdk.jmh:jmh-generator-annprocess:1.14.1'
}
```

あとは、ベンチマークを書きます。`@Benchmark` をつけるだけで使えます。

annotation processor を利用しているので、IntelliJ から起動する場合には Annotation Processor を有効にしてください。
`Build, Execution, Deployment > Compiler > Annotation Processors` から `Enable Annotation Processors` に
チェックする必要があります。 

```java
package com.example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.stream.IntStream;

public class ListBenchmark {
    @Benchmark
    public int streamSum() throws URISyntaxException {
        return IntStream.range(0, 1000)
                .sum();
    }

    @Benchmark
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
```

この main method から起動すると以下のような結果が出力されます。

```
/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/bin/java -Didea.launcher.port=7536 "-Didea.launcher.bin.path=/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/charsets.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/deploy.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/ext/cldrdata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/ext/dnsns.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/ext/jaccess.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/ext/jfxrt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/ext/localedata.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/ext/nashorn.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/ext/sunec.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/ext/sunjce_provider.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/ext/sunpkcs11.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/ext/zipfs.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/javaws.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/jce.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/jfr.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/jfxswt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/jsse.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/management-agent.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/plugin.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/resources.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/lib/rt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/lib/ant-javafx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/lib/dt.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/lib/javafx-mx.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/lib/jconsole.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/lib/packager.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/lib/sa-jdi.jar:/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/lib/tools.jar:/Users/tokuhirom/dev/java-handbook/samples/jmh/build/classes/main:/Users/tokuhirom/.gradle/caches/modules-2/files-2.1/org.openjdk.jmh/jmh-core/1.14.1/5d6686fd71204d467e10d306d8c356a14a80770f/jmh-core-1.14.1.jar:/Users/tokuhirom/.gradle/caches/modules-2/files-2.1/org.openjdk.jmh/jmh-generator-annprocess/1.14.1/830dc27f70d7036b405e3f024cb4c2fa822bca72/jmh-generator-annprocess-1.14.1.jar:/Users/tokuhirom/.gradle/caches/modules-2/files-2.1/net.sf.jopt-simple/jopt-simple/5.0.2/98cafc6081d5632b61be2c9e60650b64ddbc637c/jopt-simple-5.0.2.jar:/Users/tokuhirom/.gradle/caches/modules-2/files-2.1/org.apache.commons/commons-math3/3.2/ec2544ab27e110d2d431bdad7d538ed509b21e62/commons-math3-3.2.jar:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar" com.intellij.rt.execution.application.AppMain com.example.ListBenchmark
# JMH 1.14.1 (released 8 days ago)
# VM version: JDK 1.8.0_77, VM 25.77-b03
# VM invoker: /Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/bin/java
# VM options: -Didea.launcher.port=7536 -Didea.launcher.bin.path=/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.example.ListBenchmark.forSum

# Run progress: 0.00% complete, ETA 00:00:20
# Fork: 1 of 1
# Warmup Iteration   1: 2540968.934 ops/s
# Warmup Iteration   2: 2577273.184 ops/s
# Warmup Iteration   3: 2547454.985 ops/s
# Warmup Iteration   4: 2651007.557 ops/s
# Warmup Iteration   5: 2545635.805 ops/s
Iteration   1: 2640561.772 ops/s
Iteration   2: 2458074.014 ops/s
Iteration   3: 2367925.531 ops/s
Iteration   4: 2497981.362 ops/s
Iteration   5: 2299906.873 ops/s


Result "forSum":
  2452889.910 ±(99.9%) 501706.533 ops/s [Average]
  (min, avg, max) = (2299906.873, 2452889.910, 2640561.772), stdev = 130291.593
  CI (99.9%): [1951183.378, 2954596.443] (assumes normal distribution)


# JMH 1.14.1 (released 8 days ago)
# VM version: JDK 1.8.0_77, VM 25.77-b03
# VM invoker: /Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/jre/bin/java
# VM options: -Didea.launcher.port=7536 -Didea.launcher.bin.path=/Applications/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8
# Warmup: 5 iterations, 1 s each
# Measurement: 5 iterations, 1 s each
# Timeout: 10 min per iteration
# Threads: 1 thread, will synchronize iterations
# Benchmark mode: Throughput, ops/time
# Benchmark: com.example.ListBenchmark.streamSum

# Run progress: 50.00% complete, ETA 00:00:10
# Fork: 1 of 1
# Warmup Iteration   1: 399340.103 ops/s
# Warmup Iteration   2: 444224.252 ops/s
# Warmup Iteration   3: 370769.038 ops/s
# Warmup Iteration   4: 369619.858 ops/s
# Warmup Iteration   5: 365175.203 ops/s
Iteration   1: 361513.205 ops/s
Iteration   2: 353190.357 ops/s
Iteration   3: 372077.392 ops/s
Iteration   4: 354354.544 ops/s
Iteration   5: 358669.026 ops/s


Result "streamSum":
  359960.905 ±(99.9%) 29081.888 ops/s [Average]
  (min, avg, max) = (353190.357, 359960.905, 372077.392), stdev = 7552.474
  CI (99.9%): [330879.017, 389042.793] (assumes normal distribution)


# Run complete. Total time: 00:00:20

Benchmark                 Mode  Cnt        Score        Error  Units
ListBenchmark.forSum     thrpt    5  2452889.910 ± 501706.533  ops/s
ListBenchmark.streamSum  thrpt    5   359960.905 ±  29081.888  ops/s

Process finished with exit code 0
```

この、最後のブロックが結果になります。

```
Benchmark                 Mode  Cnt        Score        Error  Units
ListBenchmark.forSum     thrpt    5  2452889.910 ± 501706.533  ops/s
ListBenchmark.streamSum  thrpt    5   359960.905 ±  29081.888  ops/s
```

Mode thrpt はスループットを表しています。

この例でいうと、forSum は 2452889.910 ops/sec で実行可能ということになります。
streamSum は 359960.905 ops/sec なので、forSum の方が圧倒的に高速だということがわかります。

## BenchmarkMode の指定

Benchmark 結果で、スループット以外も出力したい場合は、以下の様に `@BenchmarkMode` を指定します。

```java
    @Benchmark
    @BenchmarkMode(Mode.All)
    public int forSum() throws MalformedURLException {
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            sum += i;
        }
        return sum;
    }
```

結果が以下のように細かく出力されます。用途によって使い分けるといいでしょう。

```
Benchmark                                    Mode     Cnt        Score        Error  Units
ListBenchmark.forSum                        thrpt       5  2487852.267 ± 560316.401  ops/s
ListBenchmark.streamSum                     thrpt       5   336313.403 ±  75214.358  ops/s
ListBenchmark.forSum                         avgt       5       ≈ 10⁻⁶                s/op
ListBenchmark.streamSum                      avgt       5       ≈ 10⁻⁶                s/op
ListBenchmark.forSum                       sample  160898       ≈ 10⁻⁶                s/op
ListBenchmark.forSum:forSum·p0.00          sample               ≈ 10⁻⁶                s/op
ListBenchmark.forSum:forSum·p0.50          sample               ≈ 10⁻⁶                s/op
ListBenchmark.forSum:forSum·p0.90          sample               ≈ 10⁻⁶                s/op
ListBenchmark.forSum:forSum·p0.95          sample               ≈ 10⁻⁶                s/op
ListBenchmark.forSum:forSum·p0.99          sample               ≈ 10⁻⁶                s/op
ListBenchmark.forSum:forSum·p0.999         sample               ≈ 10⁻⁵                s/op
ListBenchmark.forSum:forSum·p0.9999        sample               ≈ 10⁻⁴                s/op
ListBenchmark.forSum:forSum·p1.00          sample                0.006                s/op
ListBenchmark.streamSum                    sample  114387       ≈ 10⁻⁵                s/op
ListBenchmark.streamSum:streamSum·p0.00    sample               ≈ 10⁻⁶                s/op
ListBenchmark.streamSum:streamSum·p0.50    sample               ≈ 10⁻⁶                s/op
ListBenchmark.streamSum:streamSum·p0.90    sample               ≈ 10⁻⁵                s/op
ListBenchmark.streamSum:streamSum·p0.95    sample               ≈ 10⁻⁵                s/op
ListBenchmark.streamSum:streamSum·p0.99    sample               ≈ 10⁻⁵                s/op
ListBenchmark.streamSum:streamSum·p0.999   sample               ≈ 10⁻⁴                s/op
ListBenchmark.streamSum:streamSum·p0.9999  sample               ≈ 10⁻³                s/op
ListBenchmark.streamSum:streamSum·p1.00    sample                0.005                s/op
ListBenchmark.forSum                           ss       5       ≈ 10⁻⁵                s/op
ListBenchmark.streamSum                        ss       5       ≈ 10⁻⁴                s/op
```
