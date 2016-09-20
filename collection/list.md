# list

java.util.List は Java ではもっとも基本的なコンテナ型の一種です。
同じ型の要素を複数保持することができます。

Java には基本的な型として配列もありますが、サイズ変更が不可能ですので用途が限られます。一般的なアプリケーションでは list を利用します。

## List の構築

ざっくりいうと: *null の心配がないなら guava の ImmutableList 使おう*

### guava の ImmutableList

とにかく変更されない List を作りたいぞ！という時には、Google guava には ImmutableList の実装がありますので、こちらを利用しましょう。変更できないので *マルチスレッドで共有しても安心* です。

なお、guava の ImmutableList は、*null を入れることができません*。null を入れようとすると NullPointerException が発生しますのでご注意ください。

```java
// 空の immutable list を作成
ImmutableList<Object> of = ImmutableList.of();

// 幾つかの要素を含む Immutable list を作成
ImmutableList<Integer> of1 = ImmutableList.of(1, 2, 3);

// いっぱい入った immutable list をビルダー経由で作成
ImmutableList<Object> build = ImmutableList.builder()
        .add(1)
        .add(2)
        .add(3)
        .addAll(of1)
        .build();
```

guava の `ImmutableList.of()` を0引数で呼び出した場合には、global 変数で保持されている RegularImmutableList.EMPTY が返却されます。1引数の場合は SingletonImmutableList のインスタンスが返されます。

### Java 標準メソッドによる List の構築

#### `Arrays.asList` による Array の構築

List の構築には `Arrays.asList` を利用します。

```java
List<Integer> integers = Arrays.asList(1, 2, 3);
```

↑のコードはつまり、以下のコードと同じことです。

```java
Integer[] integers1 = {
        1, 2, 3
};
List<Integer> integers2 = Arrays.asList(integers1);
```

リスト構築用によく使われていますが、このメソッドのもともとの意味は「配列に対してアクセスする List インターフェースビューの生成」であることに注意してください。配列に対して List インターフェースでアクセス出来るので、この List にたいして変更を行った場合は配列の側にも反映されます。Java の配列は固定サイズなのでこのメソッドから得られた List も固定サイズです。

極めて紛らわしいのですが、このメソッドが返すリストは、`java.util.ArrayList` ではなく Arrays.asList 専用の `java.util.Arrays.ArrayList` であることに注意してください。同じ ArrayList という名前ですが実装がだいぶ違います。

#### `Collections.singletonList()` によるリスト構築

1要素のオブジェクトを含むリストを作成するときには `Collections.singletonList` を利用できます。

```java
List<Integer> integers = Collections.singletonList(3);
```

`max(List<Integer> integers);`  に対して `max(Collections.singletonList(3))` のように、リストを要求するメソッドの引数に1つの要素を渡したいときに使うことが多いです。

このリストは 1 要素しか含められず変更不可能である代わりにアクセスが*高速*です。
`Arrays.asList` を 1要素の配列に対して使っていると IntelliJ IDEA は `Collections.singletonList` に変えるようにサジェストします。

ImmutableList を利用している場合には `ImmutableList.of()` を1引数で読んだ場合も同等の処理がなされているので、 `ImmutableList.of()` を呼べば良いでしょう。

#### `Collections.emptyList()` で空のリストを得る

空のリストが必要になることは多々ありますが、そのたびに新しいオブジェクトを生成していたら遅いです。そこで Java が提供しているメソッドから予めアロケートされた空のリストを取得することができます(このリストは変更不可です)。

ImmutableList を利用している場合には `ImmutableList.of()` を引数なしで読んだ場合も同等の処理がなされているので、 `ImmutableList.of()` を呼べば良いでしょう。

#### `Collections.unmodifiableList()` 変更できない List のビューを返す

List を返すメソッドを実装する場合、その返した値を変更されると困ります。
`java.util.Collections.unmodifiableList()` を使うと、変更不可能なビューを作成することができます。
このメソッドで得られた List のビューを通じて変更することはできませんが、元の List を操作すると変更可能なことに注意が必要です。

実際の利用シーンです。[org.springframework.boot.configurationprocessor.metadata;
.ItemHint](https://github.com/spring-projects/spring-boot/blob/2a6b1e69b68730cc2e5be6d986fd58b0989fcb5a/spring-boot-tools/spring-boot-configuration-processor/src/main/java/org/springframework/boot/configurationprocessor/metadata/ItemHint.java)

```java
public List<ValueHint> getValues() {
	return Collections.unmodifiableList(this.values);
}
```

#### `Collections.syncrhonizedList()` による List アクセスの同期化

ざっくりいうと: *現実的なアプリケーションではほとんど使いません*

List へのアクセスを複数のスレッドから同時に行うことはできません。
List をマルチスレッドで共有して、なおかつ変更したい場合には `Collections.syncrhonizedList()`　を利用できる場合があります。synchronizedList で得られるリストは操作ごとにロックをとるようになります。しかし、ロックを取る分すべての操作が遅くなります。

```java
List list = Collections.synchronizedList(new ArrayList(...));
```

synchronizedList は個々のメソッドは複数のスレッドからのメソッド呼び出しは安全ですが、複数のメソッドの呼び出しの間で保護されているわけではないので、気をつけて使う必要があります。

```java
if (!list.contains(a)) {
  list.add(a);
}
```

現実的なアプリケーションではこのメソッドで同期化する必要があるケースはほぼ無いと思います。

#### CopyOnWriteArrayList

ざっくりいうと: *現実的なアプリケーションではほとんど使いません*

破壊的操作(add, set 等)が行われるたびに内部で保持している配列のコピーを作ることによりスレッドセーフを実現している List の実装です。

どう考えても効率が悪いですが、イテレータで全部を舐める回数が変更処理をする回数よりも圧倒的に多い場合には効率が良い場合があります。イテレータを同期できない場合や同期したくないが、並行スレッド感の干渉を排除したいときにも役立ちます。

具体例としては [org.springframework.ide.eclipse.beans.ui.BeansUIActivationHistory](https://github.com/spring-projects/spring-ide/blob/7707ecd47d164f6c25400b4f8303a2009631bce7/plugins/org.springframework.ide.eclipse.beans.ui/src/org/springframework/ide/eclipse/beans/ui/BeansUIActivationHistory.java) があります。


```java
public class BeansUIActivationHistory {

	private static List<String> HISTORY = new CopyOnWriteArrayList<String>();

	public static void clearHistory() {
		HISTORY = new CopyOnWriteArrayList<String>();
	}

	public static void addToHistory(IModelElement modelElement) {
		if (modelElement != null && modelElement.getElementID() != null) {
			HISTORY.add(modelElement.getElementID());
		}
	}

	public static Set<IModelElement> getActivationHistory() {
		Set<IModelElement> history = new LinkedHashSet<IModelElement>();
		for (String elementId : HISTORY) {
			IModelElement element = BeansCorePlugin.getModel().getElement(elementId);
			if (element != null) {
				history.add(element);
			}
		}
		return history;
	}

	public static List<IBean> getBeanActivationHistory() {
		List<IBean> history = new ArrayList<IBean>();
		for (String elementId : HISTORY) {
			IModelElement element = BeansCorePlugin.getModel().getElement(elementId);
			if (element instanceof IBean) {
				history.add((IBean) element);
			}
		}
		return history;
	}
}
```

## List 型の実装

ざっくりいうと: *99% のケースでは ArrayList を使っておけば間違いない*

一般的な List 型の実装には `ArrayList` と `LinkedList` があります。

`ArrayList` は内部に配列を持った List インターフェースの実装です。配列のサイズを大きくする必要が出てきたらより大きな別の領域が確保されてそこに既存のデータはコピーされます。一般的な LL の「配列」とほぼ同等の実装です。

`LinkedList` はリンクトリストの実装です。以下のような Node クラスを繋いでリストを実現しています。

```java
private static class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;

    Node(Node<E> prev, E element, Node<E> next) {
        this.item = element;
        this.next = next;
        this.prev = prev;
    }
}
```

LinkedList は、以下のようなフィールドを持っています。

```java
public class LinkedList<E>
    extends AbstractSequentialList<E>
    implements List<E>, Deque<E>, Cloneable, java.io.Serializable
{
  transient int size = 0;
  transient Node<E> first;
  transient Node<E> last;
}
```

LinkedList は *先頭近傍への要素追加/削除が高速* ですが、*先頭近傍及び末尾近傍以外へのインデックスアクセスが低速* というデメリットがあります。利用元メソッドがインデックスアクセスする可能性があるため、LinkedList を利用する場合には内部での利用にとどめて、外部に返り値として渡すことは避けるべきです。

以下に、実際にどの程度の速度差があるのかを示すベンチマークを示します。

### 先頭への要素追加

LinkedList のほうが高速です。

```java
@Test
public void addFirst() throws Exception {
    Benchmark benchmark = new Benchmark(new AddFirstListBenchmark());
    benchmark.run(1).timethese().cmpthese();
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
```

```
Score:

arrayList: 158 wallclock secs (138.92 usr +  2.05 sys = 140.97 CPU) @  0.01/s (n=1)
linkedList:  0 wallclock secs ( 0.02 usr +  0.02 sys =  0.04 CPU) @ 24.03/s (n=1)

Comparison chart:

                Rate  arrayList  linkedList
   arrayList  0.01/s         --       -100%
  linkedList  24.0/s    338679%          --
```

### 先頭要素の削除

LinkedList のほうが圧倒的に高速です。

```java
@Test
public void removeFirst() throws Exception {
    Benchmark benchmark = new Benchmark(new RemoveFirstBenchmark());
    benchmark.run(1).timethese().cmpthese();
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
```

```
Score:

linkedList:  0 wallclock secs ( 0.03 usr +  0.00 sys =  0.03 CPU) @ 29.75/s (n=1)
arrayList: 157 wallclock secs (141.42 usr +  2.41 sys = 143.83 CPU) @  0.01/s (n=1)

Comparison chart:

                Rate  linkedList  arrayList
  linkedList  29.8/s          --    427866%
   arrayList  0.01/s       -100%         --
```

### index アクセスの比較

以下は n 個の要素を持つ List に対して、すべての要素に index アクセスを行った場合のベンチマークです。

```java
@Test
public void indexBench() throws Exception {
    for (Integer x : Arrays.asList(1000, 10_000)) {
        System.out.printf("--- %d ---", x);
        Benchmark benchmark = new Benchmark(new IndexBenchmark(x));
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
```

LinkedList が極めて遅いことがわかります。LinkedList の場合、リストのノードを辿っていかなかいと目的の要素を取得できないので妥当ですね。

```
--- n=1000 ---
Score:

arrayList:  1 wallclock secs ( 1.02 usr +  0.01 sys =  1.03 CPU) @ 203588744.03/s (n=210520330)
linkedList:  1 wallclock secs ( 1.08 usr +  0.01 sys =  1.09 CPU) @ 2387.50/s (n=2598)

Comparison chart:

                     Rate  arrayList  linkedList
   arrayList  203588744/s         --    8527189%
  linkedList       2387/s      -100%          --
--- n=10000 ---
Score:

arrayList:  1 wallclock secs ( 1.24 usr +  0.01 sys =  1.24 CPU) @ 216567792.69/s (n=269306165)
linkedList:  1 wallclock secs ( 1.03 usr +  0.01 sys =  1.04 CPU) @ 17.37/s (n=18)

Comparison chart:

                     Rate  arrayList   linkedList
   arrayList  216567793/s         --  1246963562%
  linkedList       17.4/s      -100%           --
```

`LinkedList#get` の実装は、LinkedList は内部的に first/last のノードを保持しており、半分より前の要素にアクセスした時は first から順に、半分より後にアクセスしたときは last から順にリンクを辿っていく実装になっています。このため、取得位置によって get の速度は違うことに注意してください。
末尾のみ取得するベンチマークをとって比較しても意味がありません。

なお、`List#get` ではなくイテレータなどで順番に要素を取り出す分にはそれほど遅くありません。慣れているからと C-style の for でアクセスしたりするのではなく、イテレータなどを用いてちゃんとアクセスしないとひどい目にあうことがありえます。

以下にイテレータで各要素を取り出す例をあげます。

```java
@Test
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
```

ArrayList と LinkedList の間にそれほど大きな差がないということがわかります。

```
--- 1000 ---
Warm up: 10000


Score:

linkedList:  0 wallclock secs ( 0.07 usr +  0.03 sys =  0.09 CPU) @ 105308.61/s (n=10000)
arrayList:  0 wallclock secs ( 0.07 usr +  0.01 sys =  0.08 CPU) @ 132415.25/s (n=10000)

Comparison chart:

                  Rate  linkedList  arrayList
  linkedList  105309/s          --       -20%
   arrayList  132415/s         26%         --
--- 10000 ---
Warm up: 10000


Score:

linkedList:  0 wallclock secs ( 0.47 usr +  0.01 sys =  0.48 CPU) @ 21040.89/s (n=10000)
arrayList:  0 wallclock secs ( 0.40 usr +  0.00 sys =  0.41 CPU) @ 24679.29/s (n=10000)

Comparison chart:

                 Rate  linkedList  arrayList
  linkedList  21041/s          --       -15%
   arrayList  24679/s         17%         --
```


### List 実装ごとの使用容量の比較

[MemoryMeter](https://github.com/jbellis/jamm) を利用して各 List 実装ごとのメモリ使用量の比較をしてみます。

```java
public class ListSize {
    @Test
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
        MemoryMeter memoryMeter = new MemoryMeter();
        long objectSize = memoryMeter.measure(o);
        long objectSizeDeep = memoryMeter.measureDeep(o);
        System.out.printf("%-30s: %-10s %-10s\n",
                o.getClass().getSimpleName(),
                NumberFormat.getInstance().format(objectSize),
                NumberFormat.getInstance().format(objectSizeDeep));
    }
}
```

Java 1.8.0_77 と guava 19.0 での実行結果です：

```
ArrayList                     : 24         20,862,008
LinkedList                    : 32         40,000,072
RegularImmutableList          : 32         20,000,072
```

ArrayList と ImmutableList はほとんど差がありません。

ImmutableList.Builder と ArrayList の内部バッファは同じ速度で grow していきますが、ImmutableList.Builder#build() が必要なサイズのバッファにコピーして RegularImmutableList のインスタンスを作成するためメモリ使用量が少なくなります。

ArrayList と LinkedList との比較では 1要素あたり 20 バイト程度のオーバーヘッドがあるようです。

## FAQ

### list に比べて配列を利用するメリットはあるの？

配列のほうが要素アクセスの速度は格段に速いです。
速いですが、速度を気にして配列にしなくちゃいけないっていう時はあまり無いでしょう。
そこがボトルネックにはなりづらいです。VM の irep 等のような特殊なシーンではボトルネックになりますが。

そして、配列にはリサイズが不可能という大きな欠点がありますので。。

具体的には、配列が利用されるのは以下のようなシーンです。

 * 可変長引数の処理
 * byte 配列
 * VM を実装するときの irep
