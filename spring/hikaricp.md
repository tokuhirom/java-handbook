# hikaricp

HikariCP は Java で利用可能な Connection Pool 実装の一つです。
高速でデフォルト設定でも安全に使えることから、広く利用されています。

## Spring Boot と Conneciton Pooling

Spring Boot では Connection Pooling が組み込みでサポートされています。

spring boot のドキュメントでは [29.1.2 Connection to a production database](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html) で解説されています。

Connection Pool の実装としては以下のもののうち利用可能なものが利用されます(Tomcat pooling が最優先)。

 1. Tomcat pooling
 2. HikariCP
 3. Commons DBCP(Production での利用はお勧めしない）
 4. Commons DBCP2

自動で選択をさせるには、connection pooling ライブラリは、それぞれの実装の差異が大きすぎるため、一般的なアプリケーションでは `spring.datasource.type` プロパティを設定して、明示的にどの connection pooling ライブラリを利用するかを指定したほうが良いでしょう。

## 各 connection pooling ライブラリの比較

HikariCP の Wiki に機能面での比較がのっていますのでこちらご参照ください。

https://github.com/brettwooldridge/HikariCP/wiki/Pool-Analysis

また、HikariCP はパフォーマンス面でも優れています。

https://github.com/brettwooldridge/HikariCP

Prometheus/Dropwizard Metrics にデフォルトで対応しているなど、運用面でも良いライブラリです。

## 設定方法

以下のように設定すれば OK です。

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost/myapps
    username: root
    password: ~
    # MySQL ドライバを明示的に使用
    driver-class-name: com.mysql.jdbc.Driver
    # 明示的に hikaricp を利用することを宣言
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      maximum-pool-size: 10
      minimum-idle: 0
      idle-timeout: 100000
      max-lifetime: 3000000
      read-only: false
      connection-init-sql: "SET SESSION sql_mode='TRADITIONAL,NO_AUTO_VALUE_ON_ZERO,ONLY_FULL_GROUP_BY'"
```

`spring.datasource.hikari` の中の設定は `com.zaxxer.hikari.HikariConfig` にマッピングされています。すべての HikariCP の設定をここで行うことができます。
設定可能な項目は [HikariCP](https://github.com/brettwooldridge/HikariCP) の README に網羅されていますので、一通り目をとおしておくとよいでしょう。

得に MySQL の場合、設定していただきたいのは `spring.datasource.hikari.connection-init-sql` です。
ここで、コネクションの sql_mode を strict にしておくことにより、バグの発生を防ぐことができます。MySQL は歴史的な理由により、異常に data の validation がゆるく設定されているのですが、これをかなり strict に変更することができるようになります。
(mysqld 側の設定を変更している場合にはこの設定は必要ありません)
