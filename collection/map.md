# Map

Map は Java の基本的なコレクションの一つです。Python でいう辞書型、Perl や Ruby でいう Hash 型にあたるものです。

本章ではざっくりとどれをどういうときに使うかという指針のみ示しますので、詳しいところは javadoc を御覧ください。

## 実装

### java.util.HashMap

最もよく使う Map の実装です。順序が保証されない辞書型です。ストアされた順番でイテレータで取り出すことは保証されません。

## java.util.LinkedHashMap

順序が保証される辞書型です。java.util.HashMap よりもメモリは食いますが put した順序で値を取り出すことができます。
値をストアした順番で取り出す必要が有るときに利用します。

例えば JSON をパースした結果をストアする場合には LinkedHashMap を利用すれば元の JSON と同じ順序で再度シリアライズできます。

(LinkedHashMap には LRU を作れるようにアクセス順に順序を保持する機能などもありますが、LRU cache は自前で作るよりも google guava 等を利用したほうが良いでしょう)

## java.util.TreeMap

Red-Black ツリーによる Map の実装です。キーがソートされた状態で保持されます。

## 順序が保証されないことを示すサンプルコード

以下のようなコードがあるときに

```java
        HashMap<String, Object> hashMap = new HashMap<>();
        TreeMap<String, Object> treeMap = new TreeMap<>();
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();

        Consumer<Map<String, Object>> showItems = map -> {
            Arrays.asList("apple", "orange", "banana")
                    .stream()
                    .forEach(it -> map.put(it, it));

            System.out.println(map.entrySet().stream()
                    .map(it -> it.getKey().toString())
                    .collect(Collectors.joining(",")));
        };

        showItems.accept(hashMap);
        showItems.accept(treeMap);
        showItems.accept(linkedHashMap);
```

出力は以下のようになります。

```
orange,banana,apple
apple,banana,orange
apple,orange,banana
```

## `Collections.synchronizedSortedMap` による同期化

```java
  synchronizedMap = Collections.synchronizedSortedMap(origMap);
```

Map に対して `Collections.synchronizedSortedMap` をかけると、同期化された Map を得ることができます。

## ConcurrentHashMap

ConcurrentHashMap は便利なやつです。

読み込みはブロックされません。最後にコミットされた変更が返されます。つまり、READ COMMITTED みたいな動作をします。

Cache 用途に利用する場合は guava の CacheBuilder を利用したほうが良いケースも多いですから検討してください。

アトミックに操作するための便利なメソッドがいくつか実装されています。

### `putIfAbsent(K key, V value)`

キーが含まれてないときにストアします。操作は atomic に行われます。

### `computeIfAbsent(K key, Function<? super K,? extends V> mappingFunction)`

キーに対応する値がストアされていない場合 mappingFunction をコールして結果をストアします。操作は atomic に行われます。
キャッシュ的な用途に使えます。

```java
        concurrentHashMap.computeIfAbsent(key,
                it -> heavyFunction(it));
```

## `ImmutableMap`

Google guava は ImmutableMap を提供しています。これは一度生成されると変更不可能な Map です。
変更が不可能なのでスレッド間で共有しても安心です。

null 値をストアすることはできないことに注意してください。

```java
ImmutableMap.of(1,2,3);
ImmutableMap.builder()
  .put("foo", "bar")
  .put("hoge", "fuga")
  .build()
```

## Map の実装ごとのメモリ消費量

100万エントリーストアした場合における、メモリ消費量を計測してみました。

MemoryMeter を利用すれば以下の様にかんたんにメモリ使用量を計測することができます。

```java
public class MapSize {
    @Test
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
```

Java 1.8.0_77, guava 19.0 における計測結果です。Map そのものと中身含めた全体での容量をそれぞれ計測しています。

```
実装                : 単体        全体
HashMap             : 48         72,386,688
LinkedHashMap       : 56         80,386,704
TreeMap             : 48         71,998,072
RegularImmutableMap : 40         64,192,392

```

LinkedHashMap が想定通り link のぶんだけやや大きいことがわかります。ImmutableMap が意外と小さいことがわかりました。

この結果は型が `Map<Integer, Integer>` の場合なので、より大きいオブジェクトをストアした場合には全体的に見たときの Map 実装そのものによるメモリ使用量の影響は相対的に小さくなりますので、よほど大きい Map を作らない限りはあまり気にしすぎないほうが良いでしょう(現実的には Web アプリケーションでは100万エントリー以上の大きい Map を Java アプリケーションの中にストアする必要があることは少ないですし)。 

