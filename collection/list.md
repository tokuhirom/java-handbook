# list

java.util.List は Java ではもっとも基本的なコンテナ型の一種です。
同じ型の要素を複数保持することができます。

Java の配列はサイズ変更が不可能ですので、一般的なアプリケーションでは list の方を利用します。

## よく使う List 型の実装

### ArrayList

最も基本的な List の実装です。内部の実装は配列です。
一般的な LL の配列と同じような実装だと思っておけばOKです。

初期化時に必要なぶんアロケートされます。配列のサイズを大きくする必要が出てきたらより大きな別の領域が確保されてそこに既存のデータはコピーされます。

以下のメソッドは若干遅いです。これらのメソッドを頻繁に利用する場合には LinkedList をかわりに使うことを考慮したほうがよいでしょう。

 * `add(0, element)` は低速です。配列の中で `index` 以後の要素をすべて後ろにコピーしてずらす必要があるからです。頻繁にこのメソッドを利用する場合は LinkedList の採用を検討してください。
 * `remove(0)` は低速です。`index` 以後の要素をすべて前にずらす必要があるからです。頻繁にこのメソッドを利用する場合は LinkedList の採用を検討してください。

### LinkedList

LinkedList は、二重リンクリストの実装です。linked list について知らない場合は勉強不足なので、アルゴリズムとデータ構造について書いてある本を読んだほうが良いでしょう。

ざっくり言うと ArrayList に比べて以下のようなメリットがあります。以下のメリットが必要な場合には LinkedList の採用を検討してください。

 * List 先頭への要素追加が高速
 * List 先頭要素の削除が高速 

このような要求がある場合も多いので、割りとよく使う実装です。

一方で、ArrayList に比べて以下のデメリットがあります。

 * メモリ使用量が多め
 * index アクセスが遅い。つまり `get(100)` のようなアクセスの場合、linked list を 100 個たどらないと行けないので遅いです。

実際の利用シーンの例です[org.springframework.boot.actuate.trace.InMemoryTraceRepository](https://github.com/spring-projects/spring-boot/blob/10012cfddc5479ee9a5cbe98bfe4f76483965bd1/spring-boot-actuator/src/main/java/org/springframework/boot/actuate/trace/InMemoryTraceRepository.java):

```java
@Override
public List<AuditEvent> find(String principal, Date after, String type) {
	LinkedList<AuditEvent> events = new LinkedList<AuditEvent>();
	synchronized (this.monitor) {
		for (int i = 0; i < this.events.length; i++) {
			AuditEvent event = resolveTailEvent(i);
			if (event != null && isMatch(principal, after, type, event)) {
				events.addFirst(event);
			}
		}
	}
	return events;
}
```

実際にベンチマークとった結果がこちらになります:

#### 先頭への要素追加

LinkedList のほうが圧倒的に高速です。

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

#### 先頭要素の削除

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

## マルチスレッドと List

Java に付属している java.util.List の実装は基本的にマルチスレッド対応していません。
ほとんどの list はスレッド間で共有されることはないからです。スレッド間で共有されるという前提で実装すると、同期をとる必要が出てきてパフォーマンスが劣化します。

### `Collections.syncrhonizedList()`

とはいえCollection をマルチスレッドで共有したいシーンもあります。そういう場合には `Collections.syncrhonizedList()`　を利用します。synchronizedList で得られるリストは操作ごとにロックをとるのでスレッドセーフになります。しかし、ロックを取る分遅いです。

```java
List list = Collections.synchronizedList(new ArrayList(...));
```

### CopyOnWriteArrayList

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

### ImmutableList

とにかく変更されない List を作りたいぞ！という時には、Google guava には ImmutableList の実装がありますので、こちらを利用しましょう。変更できないのでマルチスレッドで共有しても安心です。

なお、guava の ImmutableList は、null を入れようとすると NullPointerException が発生しますのでご注意ください。

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

## 変更できない List を返す

List を返すメソッドを実装する場合、その返した値を変更されると困ります。
`java.util.Collections.unmodifiableList()` を使うと、変更不可能なビューを作成することができます。
このメソッドで得られた List を変更することはできませんが、元の List を操作すると変更可能なことに注意が必要です。

実際の利用シーンです。[org.springframework.boot.configurationprocessor.metadata;
.ItemHint](https://github.com/spring-projects/spring-boot/blob/2a6b1e69b68730cc2e5be6d986fd58b0989fcb5a/spring-boot-tools/spring-boot-configuration-processor/src/main/java/org/springframework/boot/configurationprocessor/metadata/ItemHint.java)

```java
	public List<ValueHint> getValues() {
		return Collections.unmodifiableList(this.values);
	}
```

## Arrays.asList による List 構築

List の構築においては、`Arrays.asList` が便利です。このメソッドは可変長引数を受け取ってくれるので、リスト構築用によく使われています。

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

リスト構築用によく使われていますが、このメソッドのもともとの意味は「配列に対してアクセスする List インターフェースのオブジェクトの生成」であることを忘れないで下さい。配列に対して List インターフェースでアクセス出来るので、この List にたいして変更を行った場合は配列の側にも反映されます。配列は固定サイズなのでこのメソッドから得られた List も固定サイズです。

極めて紛らわしいのですが、このメソッドが返すリストは、`java.util.ArrayList` ではなく Arrays.asList 専用の `java.util.Arrays.ArrayList` であることに注意してください。同じ ArrayList という名前ですが実装がだいぶ違います。

## `Collections.singletonList()` によるリスト構築

1要素のオブジェクトを含むリストを作成するときには `Collections.singletonList` を利用できます。

```java
List<Integer> integers = Collections.singletonList(3);
```

`max(List<Integer> integers);`  に対して `max(Collections.singletonList(3))` のように、リストを要求するメソッドの引数に1つの要素を渡したいときに使うことが多いです。

このリストは 1 要素しか含められず変更不可能である代わりにアクセスが*高速*です。
`Arrays.asList` を 1要素の配列に対して使っていると IntelliJ IDEA は `Collections.singletonList` に変えるようにサジェストします。

## `Collections.emptyList()` で空のリストを得る

空のリストが必要になることは多々ありますが、そのたびに新しいオブジェクトを生成していたら遅いです。そこで Java が提供しているメソッドから予めアロケートされた空のリストを取得することができます(このリストは変更不可です)。

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
