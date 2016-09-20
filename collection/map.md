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
