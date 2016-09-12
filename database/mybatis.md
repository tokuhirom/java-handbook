# MyBatis - RDBMS 2 object mapper

MyBatis は RDBMS の行をオブジェクトにマッピングするライブラリです。
いわゆる O/R Mapper です。

特徴としては

 * すべての SQL を普通に文字列で書くことになるため、パフォーマンス上の問題が発生しづらい
 * クエリの評価だけさっとやりやすい

といったところでしょうか。ある程度規模があるサービスで、低品質なクエリが書かれた時のリカバリがし易いです。

MyBatis は XML ファイルに SQL を書いていく方法とアノテーションでクエリを書く方法の２つの方法を選ぶことができます。
XML で記述する場合、メソッドを呼ぶ Java コードからいっきにクエリを見ることができませんし XML で書く理由がないのでアノテーションで書く方法を本ポエムでは推していきます。

弊社では現在 mybatis が一番利用されているようです。

## 例のエンティティ

例として Blog クラスを実装します。MyBatis は @NoArgsConstructor の方をコールしますが、ユーザーが手でインスタンス化する時のために @AllArgsConstructor もつけています。

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public static class Blog {
    Long id;
    String title;
}
```

## アノテーションを利用してマッパーを書く

アノテーションを利用してマッパーを記述します。
interface を定義し、アノテーションを記述していきます。
アノテーションを記述すると、MyBatis は java.lang.reflect.Proxy で実体化して処理してくれます。なので、interface だけ記述するだけで良いのです。

(TODO: java.lang.reflect.Proxy について別のファイルに記述する)
```java
interface BlogMapper {
    @Insert("INSERT INTO blog (title) VALUES (#{title})")
    @Options(useGeneratedKeys = true)
    int insert(Blog blog);

    @Update("UPDATE blog SET title=#{blog.title} WHERE id=#{id}")
    long update(@Param("id") long id, @Param("blog") Blog blog);

    @Update("DELETE blog WHERE id=#{id}")
    long delete(long id);

    @Select("SELECT COUNT(*) FROM blog")
    long count();

    @Select("SELECT * FROM blog")
    List<Blog> findAll();
}
```
INSERT, UPDATE, SELECT, DELETE それぞれにアノテーションが振られています。

### INSERT の場合

```java
@Insert("INSERT INTO blog (title) VALUES (#{title})")
@Options(useGeneratedKeys = true, keyProperty = "id")
int insert(Blog blog);
```

`#{title}` というパラメータが SQL の中に埋まっています。これは `blog.getTitle()` の結果をここに埋めるという意味になります。引数１個のメソッドの場合は主語を省略して書けるという親切さです。

以下のように、引数に blog という名前をつけることによって、その名前を利用して明示的に指定することもできます。

```java
@Insert("INSERT INTO blog (title) VALUES (#{blog.title})")
@Options(useGeneratedKeys = true, keyProperty = "id")
int insert(@Param("blog") Blog blog);
```

`@Options(useGeneratedKeys = true)` をつけた場合、auto_increment な値が entity に fill されます。擬似コードですが、以下の様なことが行われると思ってください。

```java
blog.id = `SELECT LAST_INSERT_ID()`;
```

返り値は affected rows です。JDBC で言うところの `ps.getUpdateCount()` の値です。

### UPDATE の場合

```java
@Update("UPDATE blog SET title=#{blog.title} WHERE id=#{id}")
long update(@Param("id") long id, @Param("blog") Blog blog);
```

特筆すべき点はありませんね。普通です！

返り値は affected rows です。

### DELETE の場合

```java
@Update("DELETE blog WHERE id=#{id}")
long delete(long id);
```

UPDATE に同じです。

### SELECT の場合

```java
@Select("SELECT COUNT(*) FROM blog")
long count();
```

↑のようなパターンの場合。これは返り値が long にマッピングされています。
このパターンの場合 `COUNT(*)` の値が素直に返ってきます。

```java
@Select("SELECT * FROM blog ORDER BY ${order}")
List<Blog> findAll(@Param("order") String order);
```

のようにした場合、全行が Blog オブジェクトにマッピングされて返ってきます。

ここで `${order}` という新しい記法が登場しています。`#{foo}` の場合 prepared statement の引数として渡されますが、`${order}` の場合はそのまま文字列として埋め込まれます。ORDER BY の引数に文字列を渡すことはできませんので `${order}` のようにして渡す必要があります。

## MyBatis と cache と

MyBatis には cache 機構があります。O/R Mapper の cache はかなり気をつけて利用しないと問題を引き起こしがちです。

MyBatis のデフォルト設定は

 * local cache オン
 * cache 範囲は SESSION

です。

キャッシュ範囲はデフォルトで SESSSION であり、1 web session を超えて状態が保持されることは無いようになっています。
 
また、@Update, @Delete, @Insert なメソッドの実行後にキャッシュクリア処理していますので、デフォルトのまま利用しても問題は無いでしょう。

それでも誤爆が心配なら cache 範囲を STATEMENT にすれば、より安心でしょう。
STATEMENT にした場合、SELECT 文を一回うつごとにキャッシュはクリアーされます。

```java
configuration.setLocalCacheScope(LocalCacheScope.STATEMENT);
```

なお、SELECT クエリで MyBatis の返却してきた値は cache されていますので、変更してはいけません。変更した場合、もう一度同じクエリを発行した場合に変更された値が返ってくる可能性があります。

## interface から実装を得る

interface から実装を得るには以下のような手順を踏んでいく必要があります。
ステップ数は多いですが、通常はフレームワーク等で吸収される部分ですので気にする必要は殆ど無いでしょう。しかし、手で構築していく手順も把握しておく方が、理解が深まって良いと思います。

```java
// 環境を構築
Environment environment = new Environment(
    MybatisTest.class.getSimpleName(),
    new JdbcTransactionFactory(),
    jdbcDataSource);

// 環境から設定を構築
Configuration configuration = new Configuration(environment);
// Mapper を登録していく
configuration.addMapper(BlogMapper.class);

// SessionFactoryBuilder を作る
SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
// SessionFactory を得る
SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(configuration);
// Session 開始。closeable。
try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
    // マッパーを取得
    BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

    // 全行取得してみる
    {
        List<Blog> all = mapper.findAll("id");
        log.info("all: {}", all);
    }
}
```

## MyBatis と kotlin と(あるいは Groovy と)

MyBatis の Mapper を annotation を利用して記述した場合、問題になってくるのが「Javaには複数行文字列リテラルがない」という事実である。
複雑なSQLともなると、１行にすべて書くのはキツイ。Indent をつけて書きたくなってくる。

じゃー XML を使えばいいのでは？　ということにもなるのだが、XML は避けたい。

そこで選択肢として出てくるのが alt java と呼ばれる言語郡である。

現在、Better Java と呼ばれている言語には

 * Scala
 * groovy
 * kotlin

などがあり、これらはいずれも複数行文字列リテラルがある。

これらの言語の中からチーム内でお好みのものを選ぶのが良い(scala がこの用途で利用可能かどうかは調べてないので知らない）。

現実的には groovy か kotlin を選ぶのが良いと思う。いずれにせよ interface の記述のみなので、実効速度等にはどの言語を選んでも関係がない。

現在やっているプロジェクトでは、kotlin のほうが将来性があると踏んで kotlin を採用している(2016年夏の時点)。若干、型の記述などで戸惑うこともあるが、ちょっとググればわかるし、チームメンバーにも好評なようだ。

kotlin で記述した場合は以下の様になります。

```kotlin
package com.example.dao

import com.example.entity.Blog
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface BlogDao {
    @Select("""
        SELECT * FROM blog
    """)
    fun findAll(): List<Blog>

    @Select("""
        SELECT * FROM blog WHERE id=#{id}
    """)
    fun findById(@Param("id") id: Long): Blog
}
```

## FAQ

### ID の生成まわりの実装について教えて下さい

org.apache.ibatis.executor.statement.PreparedStatementHandler.update
のへんから読んでくと良いです。

### SQL 手で書くのめんどくない？

めんどいけどマジカルな挙動に苦しめられることを考えれば気にならない。
と僕は思ってます。

### エンティティクラスを自動生成したい

mybatis-generator を利用すればできます。
得に理由はありませんが自分は利用していません。
