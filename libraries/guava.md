# guava

guava は google が提供している java 用の便利ライブラリ集。
Java 8 より昔からあるので、Java8 でコアに入った機能と重複している機能も多い。IntelliJ ならそういうものも警告してくれるので、警告がでたら素直に受け入れて JDK の
メソッドを利用するのが良い。

guava の利用シーン自体は減っているが、そういった中でもまだまだ便利な機能も多いので、便利なものを紹介する。

## `Lists.partition`

一つの List を複数の List に分割する。100 要素のリストを 10 要素のリスト１０個に分割、とかできる。便利。

```
List<Integer> bigList = ...
List<List<Integer>> smallerLists = Lists.partition(bigList, 10);
```

なお、Apache commons collections 4 でも同様のことができます。
機能に差はないので、すでに依存に入ってるほうを利用する、ぐらいの気持ちでいいと思います。

```
List<Integer> bigList = ...
List<List<Integer>> smallerLists = ListUtils.partition(bigList, 10);
```

ref. http://stackoverflow.com/questions/2895342/java-how-can-i-split-an-arraylist-in-multiple-small-arraylists

## コンテナインスタンスの作成

### `Sets.newHashSet("foo", "bar")`

    HashSet<String> strings = Sets.newHashSet("foo", "bar");

`Arrays.asList(a, b, c)` で List の構築が簡単にできますけれど、Set の構築が簡単にできるメソッドが Java 8 の時点では存在していません。
そこで、Set の構築には guava の便利メソッドを利用するのが一般的です。
HashSet のインスタンスを作成します。標準ライブラリには相当するものが無いので、 guava を利用すると便利です。

JDK9 では `Set.of` でできるようになります。

ref. http://jyukutyo.hatenablog.com/entry/2016/01/04/182402

## オンメモリキャッシュ

guava のオンメモリキャッシュ機構は良く出来ているので利用すると良いでしょう。
使い方は以下の通り。

    LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
           .maximumSize(1000)
           .expireAfterWrite(10, TimeUnit.MINUTES)
           .removalListener(MY_LISTENER)
           .build(
               new CacheLoader<Key, Graph>() {
                 public Graph load(Key key) throws AnyException {
                   return createExpensiveGraph(key);
                 }
               });

Web アプリケーションの場合、JVM のオンメモリにあまりたくさんのデータを入れると GC 対象が増えてしまって GC にかかるコストが増大するので節度を持ってキャッシュすることが必要です。
得に、スケールアウトする構成になっている場合には、redis 等で共有キャッシュを持つことも考慮に入れるべきでしょう。

## apache commons と guava

両方とも言語コアの機能の足りないところを補うことを目標に開発されたライブラリなので機能がわりとかぶっています。

大差ないし、なんかいろいろと maven で依存入れてると両方とも依存に入ってきがち。
お好みで使い分ければよろしい。
