# spring-boot + MyBatis

spring boot と MyBatis の組み合わせで利用するためには mybatis-spring-boot-autoconfigure を利用します。

build.gradle に以下のように依存を記述します(最新版使わないとプロパティ名が違ったりしてハマるので注意)。

```groovy
dependencies {
    compile 'org.mybatis.spring.boot:mybatis-spring-boot-starter:1.1.1'
    compile 'org.mybatis:mybatis-typehandlers-jsr310:1.0.1'
}
```

application.yml に以下のように記述します。

```yaml
mybatis.config-location: classpath:/mybatis/mybatis-config.xml
```

`src/main/resources/mybatis/mybatis-config.xml` に以下のような XML を置きます。

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <!-- lower case を camelCase にマッピングする設定。 -->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <setting name="defaultFetchSize" value="100"/>
        <setting name="defaultStatementTimeout" value="30"/>
        <!-- Throw exception when auto property mapping failed -->
        <setting name="autoMappingUnknownColumnBehavior" value="FAILING"/>
    </settings>
</configuration>
```

(このへん、煩雑なので設定を YAML に書けたらいいなあと思っていて、とりあえず issue 登録しました https://github.com/mybatis/spring-boot-starter/issues/94)

基本的にはこれだけで mybatis が利用可能です。
(DataSource の取得については後で書く)
