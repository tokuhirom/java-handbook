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
# lower_case のカラム名を camelCase のプロパティにマッピングする
mybatis.configuration.map-underscore-to-camel-case: true
mybatis.configuration.default-fetch-size: 100
mybatis.configuration.default-statement-timeout: true

# マッピング不能なフィールドがあったら throw (開発時のみオンにすると良いでしょう)
mybatis.configuration.auto-mapping-unknown-column-behavior: FAILING
```

mybatis-boot-starter は `@Mapper` アノテーションが書いてあるクラスをスキャンするので、各マッパークラスには `@Mapper` アノテーションをお忘れなく。

基本的にはこれだけで mybatis が利用可能です。
(DataSource の取得については後で書く)
