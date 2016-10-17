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

自動で選択をさせるには、connection pooling ライブラリは、それぞれの実装の差異が大きすぎます。
一般的なアプリケーションでは `spring.datasource.type` プロパティを設定して、明示的にどの connection pooling ライブラリを利用するかを指定したほうが良いでしょう。

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

とくに MySQL の場合、設定していただきたいのは `spring.datasource.hikari.connection-init-sql` です。
MySQL は歴史的な理由により、デフォルトの設定だと「カラムに不正な値を挿入したときに警告」しますが、sql_mode を traditional にすると「カラムに不正な値を挿入したときにエラーを返し」ます。
(mysqld 側にこの設定をすることもできますが、クライアント側に設定したほうがサーバー側での設定しわすれなどの環境間の差異を無視できるので僕はクライアント側でこの設定をしています)

see also http://www.songmu.jp/riji/entry/2015-07-08-kamipo-traditional.html
