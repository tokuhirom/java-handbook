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

`spy()` メソッドにオブジェクトを渡すと、スパイにラッピングされたオブジェクトを得ることができる。
`verify()` メソッドで、メソッドが呼ばれたかどうかを事後確認できる。

    // Dog クラスをspyにする
    ArrayList<String> list = Mockito.spy(new ArrayList<>());

    // add メソッドを呼んでみる
    list.add("hoge");

    // そのメソッドが実際に呼ばれたかどうかを確認する
    verify(list).add("hoge");

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
