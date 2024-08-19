# junit5

junit 5 が出そうということで、junit5 のメモ。

http://junit.org/junit5/docs/current/user-guide/

なんかこのページによくまとまってるから改めて書く必要があんまないな。

## テストケースの作成

Java の世界では１個の実装に対して１個のテストクラスを実装するのが基本です。

IntelliJ の場合、実装テストを Cmd+Shift+T でテストケースをサッと作ることができます。

## build.gradle の書き方

以下のように書くと良い。

<<< ./build.gradle.kts{kotlin}

## @Test

<<< ./src/test/java/com/example/HogeTest.java{java}

@Test というアノテーションをつけたメソッドがテストケースとして実行されます。テストメソッドを書いたらば、カーソルをテストメソッドの中においておいて Cmd+Shift+R で実行できます。

## data driven tests

## conclusion
