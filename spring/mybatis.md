# spring-boot + MyBatis

spring boot と MyBatis の組み合わせで利用するためには mybatis-spring-boot-autoconfigure を利用します。

build.gradle に以下のように依存を記述します(最新版使わないとプロパティ名が違ったりしてハマるので注意。開発が活発なので常に最新版を使うのがお勧めです)。

```groovy
dependencies {
    compile 'org.mybatis.spring.boot:mybatis-spring-boot-starter:1.1.1'
    compile 'org.mybatis:mybatis-typehandlers-jsr310:1.0.1'
}
```

application.yml に以下のように記述します。

```yaml
mybatis:
  configuration:
    # lower_case のカラム名を camelCase のプロパティにマッピングする
    mapUnderscoreToCamelCase: true
    defaultFetchSize: 100
    defaultStatementTimeout: 30
    # マッピング不能なフィールドがあったときの処理。
    # NONE: 何もしない
    # WARNING: WARN ログを出す(お勧め。org.apache.ibatis.session.AutoMappingUnknownColumnBehavior で WARN です)
    # FAILING: Fail mapping (Throw SqlSessionException) (local 開発時のみオンにすると良いでしょう)
    autoMappingUnknownColumnBehavior: FAILING    
  # タイプハンドラの適用
  type-handlers-package: com.example.typehandler
```

mybatis-boot-starter は `@Mapper` アノテーションが書いてあるクラスをスキャンするので、各マッパークラスには `@Mapper` アノテーションをお忘れなく。

基本的にはこれだけで mybatis が利用可能です。
(DataSource の取得については後で書く)

詳細については [mybatis-spring-boot-starterの使い方
](http://qiita.com/kazuki43zoo/items/ea79e206d7c2e990e478#mapper%E3%82%A4%E3%83%B3%E3%82%BF%E3%83%95%E3%82%A7%E3%83%BC%E3%82%B9%E3%81%AE%E3%82%B9%E3%82%AD%E3%83%A3%E3%83%B3%E3%81%AE%E4%BB%95%E7%B5%84%E3%81%BF) が詳しいのでご参照ください。

## IntelliJ で MyBatis のアノテーションに injection したい

[assets/mybatis-intellij-language-injections.xml]() にアノテーションから自動設定するための XML ファイルを置きましたので、
各位で以下の画像を参考に設定してください。手で @Select, @Insert, @Delete, @Update にそれぞれ Cmd+Enter おして設定していっても同様の効果が得られます。

[![https://gyazo.com/8dc814cb5e426f0cfbf722742bc33e9a](https://i.gyazo.com/8dc814cb5e426f0cfbf722742bc33e9a.png)](https://gyazo.com/8dc814cb5e426f0cfbf722742bc33e9a)

Database との連携設定をすると、annotation の中でも SQL のテーブル名・カラム名を補完しながらかけますので設定したほうが開発効率があがります。

Database との連携設定についてはこちら参照してください。
https://youtu.be/E8nX707UC9k
