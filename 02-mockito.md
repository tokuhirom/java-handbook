# mockito

モッキングのために　mockito 使うことを知るといろいろ便利です。

http://mockito.org/

## mockito を使ってみる

例えば次のような Dog クラスがあるとする。この interface を実装した mock クラスを動的に生成したいというケース。

    interface Dog {
        String berk();
    }

次のように `mock()` メソッドを呼ぶと、モック化されたインスタンスが得られます。
`mock()` メソッドで得られたモックオブジェクトを利用して `when()` を呼ぶと、特定の引数を渡した時の挙動を設定できます。

    // Dog クラスをモッキングする
    Dog dog = mock(Dog.class);

    // berk メソッドを引数なしで呼んだ時には "わんわん” を返すように設定
    when(dog.berk()).thenReturn("わんわん");

    // 設定したものが返ってきていることを確認
    assertThat(dog.berk()).isEqualTo("わんわん");

これはつまり、素の java で書けば以下の様に書くのと同様です。

    // Dog クラスをモッキングする
    Dog dog = new Dog() {
        @Override
        public String berk() {
            return "わんわん";
        }
    };

    // 設定したものが返ってきていることを確認
    assertThat(dog.berk()).isEqualTo("わんわん");

素の java で書けるなら mockito など要らないじゃないか、と思う方も多いでしょう。
実際そのような意見を持つエンジニアもいるようです。

しかし、今回のような単純な１メソッドしかない interface を実装する場合ではなく複数のメソッドをもつ interface のうち１つのメソッドだけが必要なケースのモッキングしたいケースなどには
mockito で特定のメソッドだけを when で mocking したほうが楽です。

## when でできるいろいろなこと

### 引数のマッチング

`when()` の引数として、次のように　`"hoge"`　を渡した場合、`"hoge"` が引数として渡ってきた場合に true が返却されるように設定されます。
(得に設定されていない呼び出しについては null が返ります)

    Printer printer = mock(Printer.class);
    when(printer.printMessage("hoge"))
            .thenReturn(true);
    assertThat(printer.printMessage("hoge"))
            .isTrue();

引数に渡ってくる文字列の指定がめんどくさい時には `anyString()` のような抽象的なマッチャを渡すこともできます。

        Printer printer = mock(Printer.class);
        when(printer.printMessage(anyString()))
                .thenReturn(true);
        assertThat(printer.printMessage("hoge"))
                .isTrue();

`foo.bar("hoge", anyString()` のように　"hoge"` のような実引数とマッチャを混在させて指定することはできません。

よく使われるのは以下のマッチャです。

| `anyString()` | 任意の文字列にマッチ |
| `anyObject()` | 任意のオブジェクトにマッチ。ただしメソッドがオーバーロードされている場合には `any(Class<T>)` のように型を明示しないと行けない場合があります | 
| `any(Class<T>)` | 任意のクラスのインスタンスにマッチ | 
| `eq(T)` | `equals()` が true を返す場合にマッチ | 

when であまりがんばって条件を指定しすぎない方が良いです。
mockito で作成されたオブジェクトはデフォルトでは呼び出しに対して null を返すため、when でのマッチャ条件から外れると null が返り、NullPointerException が発生します。

mockito により発生する NullPointerException は発見が困難なのでイラッとします。条件にマッチしなかったことを発見するのが現状の mockito だと難しいので、適当に全部 any を指定しておくのが
良い気がします。実際呼ばれたかどうかの確認は結局 verify でやるので。

### thenThrow

以下のように `thenThrow` メソッドを呼ぶと、呼ばれた時に例外を投げるようにできます。

    // Dog クラスをモッキングする
    Dog dog = mock(Dog.class);

    // 呼んだら IllegalStateException が発生するようにする
    when(dog.berk())
            .thenThrow(IllegalStateException.class);

    // 設定したものが返ってきていることを確認
    assertThatThrownBy(dog::berk)
            .isInstanceOf(IllegalStateException.class);


## spy からの verify

`spy()` メソッドにオブジェクトを渡すと、スパイにラッピングされたオブジェクトを得ることができます。
もともとある普通のオブジェクトに対して、カウント用の Proxy をかぶせている感じです。

`verify()` メソッドで、メソッドが呼ばれたかどうかを事後確認できます。

`verify()`　メソッドは `mock()` で作成されたオブジェクトでも利用できます。 

    // Dog クラスをspyにする
    ArrayList<String> list = Mockito.spy(new ArrayList<>());

    // add メソッドを呼んでみる
    list.add("hoge");

    // そのメソッドが実際に呼ばれたかどうかを確認する
    verify(list).add("hoge");

## 返り値が void なメソッドの挙動を設定する

返り値が void なメソッドは `when(mock.callMethod()).thenReturn()` 形式で挙動を設定することはできません。
`when()` の引数として void なメソッドは設定できませんからね。。

そういう時の回避策として `doThrow()` 等のメソッドが用意されています。
`when()` 利用する場合、void の場合だけこの記法使わざるを得ず、複数の記法が混在するのがイヤでこちらの記法をメインで利用している方もいるようです。

    Gah mock = mock(Gah.class);

    doThrow(IllegalArgumentException.class).when(mock).bar();

    assertThatThrownBy(mock::bar)
            .isInstanceOf(IllegalArgumentException.class);

同系統のメソッドとして他にも以下のメソッドが利用できます。

 * doCallRealMethod
 * doAnswer
 * doNothing
 * doReturn

## アノテーションを利用してやる

業務で書く場合にはアノテーション利用するのが良いでしょう。
`@RunWith(MockitoJunitRunner.class)` をクラスに指定するだけなので簡単です。

    interface Pet {
        String berk();
    }
    
    class Human {
        Pet pet;
    
        String berkPet() {
            return pet.berk();
        }
    }
    
    @RunWith(MockitoJUnitRunner.class)
    public class MockitoAnnotationTest {
        // @Mock 指定されたものは mock() される
        @Mock
        Pet pet;
    
        // @Mock 指定されたフィールドの中身が @InjectMocks 指定されたクラスに inject される。
        @InjectMocks
        private Human underTest;
    
        @Test
        public void test() {
            when(pet.berk()).thenReturn("foo");
    
            assertThat(underTest.berkPet())
                    .isEqualTo("foo");
        }
    }

アノテーションが増えるので学習コストが増大してしまいますが、シンプルに書けて良いです。
自分も業務ではこれを多用しています。

### Mockito code generator

IntelliJ では [Mockito Code Generator](https://plugins.jetbrains.com/plugin/7901?pr=) というプラグインが
あります。これを入れると、`Cmd+Shit+T` でテストケースを生成した後 `Cmd+Shift+M` 打つと、元のクラスを `@InjectMocks` に指定して
存在しているフィールドすべてを `@Mock` 指定したソースコードを生成できます。

InjtelliJ で　Mockito 使う場合にはマジでお勧めなので利用したほうがいいです。

## FAQ

### 返り値が void なメソッドを `when` で書き換えたいのですが

doReturn とかでできるけど TBD

### static メソッドを置き換えたいのですが

powermock なら static メソッドも差し替えられるゾ。

ただ、、static メソッドを mockito で書き換えないと行けないケース、そもそも設計が間違っています。
差し替えたくなるようなものを static メソッドで実装するべきではありません。

該当の static メソッドが自分が書いたコードなら修正すべきです。

### final クラスがモッキングできない

final　指定というのは継承ができないという指定です。
 
継承ができないということは、継承した子クラス作れないってことなんで、それはまあモッキングもできないでしょう、と。

モッキングされる可能性があるようなクラスは final 指定しないようにする必要があります。

### EasyMock とはどう違うの？

僕は EasyMock 使ったことないので知りません

### Mockito の 2.0 っていつ出るの？

いつ出るんでしょうねえ。2.0 向けにすでにパッチ送って取り込まれてるのとかあるしはやく出て欲しい

## スタブし忘れて NullPointerException 発生するのが辛いです

mockito では mocking していない場合、`org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues` が返ります。
JDK で提供されているコンテナ型の場合には空のコンテナ型が返りますのでいいのですが、通常のユーザーが作成したクラスの場合、null が返ります。
これにより NullPointerException が発生してつらいです。

SmartNullPointerException が発生した場合、以下のようなエラーメッセージが表示され、非常に問題が解決しやすくなります。

```
org.mockito.exceptions.verification.SmartNullPointerException: 
You have a NullPointerException here:
-> at com.example.mockito.MockitoReturnsSmartNullsTest.test(MockitoReturnsSmartNullsTest.java:20)
because this method call was *not* stubbed correctly:
-> at com.example.mockito.MockitoReturnsSmartNullsTest.test(MockitoReturnsSmartNullsTest.java:20)
foo.boz();

	at com.example.mockito.MockitoReturnsSmartNullsTest.test(MockitoReturnsSmartNullsTest.java:20)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:78)
	at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:57)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
```


Mockito 2.0 以後ではデフォルトが ReturnsEmptyValues から ReturnsSmartNulls に変更されており、NullPointerException が発生するようになった原因を教えてくれるようになります。
Mockito 2.0 でそうなるのであれば先取りしたいところですね。

when で指定されていないメソッドのデフォルトの挙動をグローバルに変更するには `org.mockito.configuration.MockitoConfiguration` クラスを実装すればよいです。
この名前は `org.mockito.internal.configuration.ClassPathLoader` にハードコードされてますので、変更は不可能です。
以下のクラスを設置するだけで、利用できるので簡単です。

```
package org.mockito.configuration;

import org.mockito.stubbing.Answer;

// https://solidsoft.wordpress.com/2012/07/02/beyond-the-mockito-refcard-part-1-a-better-error-message-on-npe-with-globally-configured-smartnull/
// See org.mockito.internal.configuration.ClassPathLoader
public class MockitoConfiguration extends DefaultMockitoConfiguration {
    public Answer<Object> getDefaultAnswer() {
        return new ReturnsSmartNulls();
    }
}
```

この設定はライフチェンジングなのでマジでお勧めです。

