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

なお、Apache commons collections 4 でも同様のことができる。機能に差はないので、すでに依存に入ってるほうを利用する、ぐらいの気持ちでいいと思います。

```
List<Integer> bigList = ...
List<List<Integer>> smallerLists = ListUtils.partition(bigList, 10);
```

ref. http://stackoverflow.com/questions/2895342/java-how-can-i-split-an-arraylist-in-multiple-small-arraylists


## apache commons と guava

両方とも言語コアの機能の足りないところを補うことを目標に開発されたライブラリなので機能がわりとかぶっています。

大差ないし、なんかいろいろと maven で依存入れてると両方とも依存に入ってきがち。
お好みで使い分ければよろしい。
