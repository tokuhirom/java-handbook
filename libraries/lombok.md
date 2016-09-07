# lombok

Java で普通にコードを書いていく場合、getter/setter 等を手で書かなくてはいけません。
C# ならば

    class Shape
    {
        public double Area { get; set;}
    }

などと書くだけで済むところも、Java だと次のようにあたたかみのある手書き getter/setter で記述しなくてはなりません。

    public class Shape {
        private double area;
        public double getArea() {
            return area;
        }
        public void setArea(double area) {
            this.area = area;
        }
    }

自動で生成可能なコードを手書きすると、本質的な処理が覆い隠されてしまい、コードが読みづらくなると同時に、保守性が低下します。
lombok を利用すれば、アノテーションを付与するだけで getter/setter や equals, hashcode, toString などの自動生成可能なメソッドを自動生成できます。

## 最低限覚えておいたほうがいいアノテーション

### @Slf4j

ロガークラスのインスタンスを生成してくれます。

     @Slf4j
     public class LogExample {
     }

と記述すると以下のコードが生成されます：

     public class LogExample {
         private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
     }

ロガーのインスタンス作成コードは邪魔くさいので、`@Slf4j` で省略したほうが良いでしょう。

### @Value

Immutable なクラスを生成するためのアノテーションです。

    @Value
    public class Foo {
        private bar;
    }

@Value を指定すると以下のアノテーションを付与したのと同等の効果が得られます。Getter は生成されますが、Setter は生成されません。
すべてのフィールドを設定するコンストラクタが生成されますので、オブジェクト生成時にのみフィールドは設定可能となります。

    @Getter
    @FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode

エンティティクラスにはこれを利用すれば良いです。

### @Data

Mutable なクラスを生成するためのアノテーションです。

フレームワークやライブラリの制限で Immutable なクラスを扱えない場合にはこちらを利用します。

以下のアノテーションを付与したのと同等の効果があります。

    @ToString
    @EqualsAndHashCode
    @Getter
    @Setter
    @RequiredArgsConstructor 

基本的には @Value を利用するようにしたほうがいいです。Immutable にできる部分については、できるかぎり Immutable にしたほうが、コードをクリーンに保つことができます。 
最近は jackson も @Value に対応しており、@Data の出番は減ってきているようです。

@Data を利用している場合にも、全部の値を受け取るコンストラクタが欲しくなることがあるでしょう。
そのような場合には以下のように書けば、引数なしのコンストラクタと、全部入りコンストラクタの両方が生成されます。

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public class Foo {
        private int hoge;
        private int fuga;
    }

## プリミティブなアノテーション

### @Getter, @Setter

getter/setter を自動生成します。フィールドやクラスに対して付与可能です。

フィールドに対して付与した場合、そのフィールドに対して getter/setter が生成されます。

    public class Foo {
        @Getter @Setter
        private int bar;
    }

クラスに対して付与した場合は、生成可能なすべてのフィールドに対して getter/setter が生成されます。

    @Getter @Setter
    public class Foo {
        private int bar;
    }

現実的には @Getter, @Setter を明示的に利用するよりも @Value, @Data を利用するシーンのほうが多いです。

## @AllArgsConstructor

クラスに対して設定します。

    @AllArgsConstructor
    public class Foo {
        private int hoge;
        private int fuga;
    }

のように設定すると、以下のようなクラスが生成されます。

    public class Foo {
        private int hoge;
        private int fuga;
        
        public Foo(int hoge, int fuga) {
            this.hoge = hoge;
            this.fuga = fuga;
        }
    }

Immutable なクラスを作成したい時に便利です。通常は @Value によって設定したほうが良いでしょう。 

## 議論の余地があるアノテーション

### @SneakyThrows

使ってはいけません。例外ハンドリングが追いづらくなります。
処理しないならしないで、素直に throws で投げましょう。

### @Builder

@Builder は、ビルダークラスを自動的に生成してくれます。
fluent interface でインスタンスを生成できて一見便利なのですが、@AllArgsConstructor あるいは @RequiredArgsConstructor を利用したほうが、必要なフィールドを指定し忘れる危険がなくて良いです。
@Builder を利用した場合、後からフィールド追加するとフィールド未指定になる危険性があります。@AllArgsConstrucotr の場合にはコンパイルエラーになるので検出が容易です。

@AllArgsConstructor の場合には、どの引数がなにに当たるのかわかりづらいな、というのはもっともなのですが、IntelliJ では `Cmd+P`　を押すとどの引数がどれにあたるのか表示されるので意外と困りません。 

## lombok と IntelliJ IDEA

https://plugins.jetbrains.com/plugin/6317

lombok plugin はとにかく必須プラグインなのでインストールしましょう。

### Enable annotation processing

[![https://gyazo.com/da9dbd3cf601e6adf4b03ed207caf19e](https://i.gyazo.com/da9dbd3cf601e6adf4b03ed207caf19e.png)](https://gyazo.com/da9dbd3cf601e6adf4b03ed207caf19e)

lombok は annotation processing という仕組みを利用して動作するのですが、IntelliJ はデフォルトでは annotation processing が無効に設定されています。
lombok のアノテーションが効いていない場合は、ここを有効にしてください。

## lombok.config

lombok は設定ファイルがあります。コンパイル時に参照されるので、プロジェクトのルートディレクトリなどに置いておけばOKです。

    lombok.addGeneratedAnnotation = false

など、様々設定できます。

かなりいろいろ設定できますので、lombok のドキュメントを参照してみてください。
https://projectlombok.org/features/configuration.html

lombok.config の中で、アノテーションの挙動をいろいろと変更することも可能ですが、やり過ぎると学習コストが増えますのでご注意ください。
