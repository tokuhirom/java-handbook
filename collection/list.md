# list

java.util.List は Java ではもっとも基本的なコンテナ型の一種です。
同じ型の要素を複数保持することができます。

Java の配列はサイズ変更が不可能ですので、一般的なアプリケーションでは list の方を利用します。

## よく使う List 型の実装

### ArrayList

最も基本的な List の実装です。内部の実装は配列です。
一般的な LL の配列と同じような実装だと思っておけばOKです。

初期化時に必要なぶん malloc されます。配列のサイズを大きくする必要が出てきたらより大きな別の領域が確保されてそこに既存のデータはコピーされます。

 * `get` は高速です
 * `add(E)` は高速です
 * `add(index, element)` は低速です。配列の中で `index` 以後の要素をすべて後ろにコピーしてずらす必要があるからです。頻繁にこのメソッドを利用する場合は LinkedList の採用を検討してください。
 * `remove(index)` は低速です。`index` 以後の要素をすべて前にずらす必要があるからです。最後の要素を取り除くときは高速です。頻繁にこのメソッドを利用する場合は LinkedList の採用を検討してください。
 * `remove(Object o)` は低速です。上に同じ理由です。
 * `boolean addAll(Collection<? extends E> c)` は高速です。

### LinkedList

LinkedList は、二重リンクリストの実装です。linked list について知らない場合は勉強不足なので、アルゴリズムとデータ構造について書いてある本を読んだほうが良いでしょう。

ざっくり言うと ArrayList に比べて以下のようなメリットがあります。以下のメリットが必要な場合には LinkedList の採用を検討してください。

 * List 先頭への要素追加が高速 
 * List 末尾への要素追加が高速 
 * 要素の削除が高速 

このような要求がある場合も多いので、割りとよく使う実装です。

## マルチスレッドと List

Java に付属している java.util.List の実装は基本的にマルチスレッド対応していません。
ほとんどの list はスレッド間で共有されることはないからです。スレッド間で共有されるという前提で実装すると、同期をとる必要が出てきてパフォーマンスが劣化します。

とはいえCollection をマルチスレッドで共有したいシーンもあります。そういう場合には `Collections.syncrhonizedList()`　を利用します。synchronizedList で得られるリストは、mutex でロックがかかるので、スレッドセーフになります。

```java
List list = Collections.synchronizedList(new ArrayList(...));
```

多くの場合は、マルチスレッドで共有される List は一度動き出してしまえば変更されることはありません。変更されない List であることが保証されていれば、値の取得時に同期を取らなくても良いので高速です。List から変更不可能な List を生成するメソッドとして `java.util.Collections.unmodifiableList` が提供されています。

```java
List<Integer> integers = Collections.unmodifiableList(Arrays.asList(1, 2, 3))
```

Google guava には ImmutableList の実装がありますので、こちらを利用しても良いです。

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

このリストは 1 要素しか含められず変更不可能である代わりにアクセスが*高速*です。
`Arrays.asList` を 1要素の配列に対して使っていると IntelliJ IDEA は `Collections.singletonList` に変えるようにサジェストします。

## `Collections.emptyList()` で空のリストを得る

空のリストが必要になることは多々ありますが、そのたびに新しいオブジェクトを生成していたら遅いです。そこで Java が提供しているメソッドから予めアロケートされた空のリストを取得することができます。

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
