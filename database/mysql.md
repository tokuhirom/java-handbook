# mysql connector/j

https://dev.mysql.com/downloads/connector/j/

java から mysql に接続するためには connector/j と呼ばれる JDBC ドライバをインストールする必要があります。build.gradle で依存に追加しましょう。

http://search.maven.org/#artifactdetails%7Cmysql%7Cmysql-connector-java%7C6.0.4%7Cjar

mysql connector/j を利用する上では、いくつか気をつけておかないといけないことがあるので以下にいくつかピックアップして紹介します。

## 型と mysql connector/j

http://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-type-conversions.html

得に、mysql connector/j を利用する場合に気をつけるべきは型です。
LL で普段プログラミングしている人が Java を始めると特にこのへんが雑になりがちです。

### 数字型

得に数字型の処理に気をつけてください。何も考えずに java の型にマッピングすると、いざ大きい数字が入った時にオーバーフローする可能性があります。

| MySQL type      | Java type            | 範囲 |
| --------------- | -------------------- | --- |
| bigint          | long                 | -9223372036854775808〜9223372036854775807 |
| bigint unsigned | java.math.BigInteger | 0〜18446744073709551615 | 
| int             | int                  | -2147483648〜2147483647   |
| int unsigned    | long                 | 0〜4294967295　　　　　　　|

特に、何も考えずに bigint unsigned 型を利用すると Java には unsigned 64 bit 整数型または 128 bit 整数型がないので java のプリミティブタイプで表現できる範囲を超えてしまい、java.math.BigInteger にマッピングされます。java.math.BigInteger 型は処理が遅いしメモリも食います。

(個人的には最近は、得に理由がなければ数値は bigint 型にマッピングしています)

### 文字列型とバイト列型

Perl のようなバイト列と文字列型の区別が曖昧な言語で DB 設計してきた人は、文字列を検索するときのオーダー指定/文字コード自動変換などを抑制したいという理由で VARCHAR を指定すべき場面で VARBINARY を指定しているようなケースがあるようです。

このような指定を Java で行うと、Java ではこれは `byte[]` 型にマッピングされるため、文字列として取り込むときには更に変換が必要になってしまいます。

きちんと文字列型は文字列型として mysql の型もつけてあげる必要があることを意識しなくてはなりません。

## MySQL Connector/J のチューニング

### `SHOW VARIABLES` と `SHOW COLLATION` の結果のキャッシュ

MySQL connector/j は接続時に `SHOW VARIABLES`　と `SHOW COLLATION` を発行し、サーバー側の設定を取得するようになっています。この処理は比較的重いので、キャッシュするオプションがあります。それが cacheServerConfiguration です。このオプションはデフォルトで false ですが、true にして運用することをおすすめします。[かなりパフォーマンスに差がでるという報告](http://www.slideshare.net/idhatak/mysql-casual)があります。

現実的なウェブアプリケーションでは、普通は `SHOW VARIABLES` と `SHOW COLLATION` の結果は運用途中には変化しないように運用しますので、キャッシュさせてしまって問題がないです。

### `useServerPrepStmts` は指定しない

得に Oracle などのまともな RDBMS を利用していた方はこのオプションを設定したがるのですが、MySQL の場合には設定する積極的な理由がありません。サーバーサイドプリペアドステートメントを利用しても実行計画がキャッシュされないからです。

ref.

 * http://d.hatena.ne.jp/kazuhooku/20081224/1230084621

MySQL のサーバーサイドプリペアドステートメントは、コネクションの処理中に「明示的に」プリペアードステートメントをクローズしないと、そのプリペアードステートメントの情報が *サーバーサイド* に残ります。これにより、大量のコネクションがサーバーサイドプリペアドステートメントを利用した場合、mysqld が不安定にな状態になりやすくなります。
