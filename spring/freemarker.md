# freemarker - template engine

freemarker は人気がある Java の template engine です。
以下は spring と連携して利用する前提で記述していますが、spring 利用しない場合でも利用できる知見があると思います。

freemarker を spring-boot で利用するには以下のように依存を追加します。

```groovy
buildscript {
    ext {
        springBootVersion = '1.4.0.RELEASE'
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
    }
}

dependencies {
  compile('org.springframework.boot:spring-boot-starter-freemarker')
}
```

## 基本的な設定

`src/main/resources/config/application.yml` に profile 切り替え用の設定を記述します。
ここではデフォルトでは以下の 2 つの profile を設定しています。

 * local: local での開発に使用。デバッグしやすく設定。
 * release: 本番環境用の設定。パフォーマンスが出るように設定。

```yaml
# ---------------------------------------------------------------------------------------------------------------------
#
# 以下、profile ごとの設定
#
# ---------------------------------------------------------------------------------------------------------------------
---
spring.profiles: default
spring.profiles.active: local
---
spring.profiles: local
spring.profiles.include: "\
  freemarker-devel,\
  freemarker-common
"
---
spring.profiles: release
spring.profiles.include: "\
   freemarker-release,\
   freemarker-common
"
```

`src/main/resources/config/application-freemarker-common.yml` に以下のように設定します。
このファイルには環境によらない fremarker の共通の設定を記述します。

```yaml
spring.freemarker:
  charset: UTF-8
  settings:
    # default charset for string?url()
    url_escaping_charset: UTF-8
    # no auto commify
    number_format: 0.#######
    # Enable auto escape
    # http://freemarker.org/docs/dgui_misc_autoescaping.html
    output_format: HTMLOutputFormat
    lazy_auto_imports: true
    auto_import: /spring.ftl as spring
```

`src/main/resources/config/application-freemarker-devel.yml` を以下のように設定します。
キャッシュをオフにして、テンプレートファイルを変更したときにをファイルシステムから随時リロードできるようにして開発効率をあげます。
例外発生時にはエラーを上げるようにします。

```yaml
spring.freemarker:
  # テンプレートのキャッシュ。
  # 開発時は false。本番では true。
  cache: false
  # 開発時は source directory を指定することにより、自動的にリロードされます。
  template-loader-path:
    - file:src/main/resources/templates/
    - classpath:/templates/
  settings:
    # 例外の処理モード。
    # 開発時は html_debug 本番は rethrow を指定する。
    template_exception_handler: html_debug
```

## Java 8 Date & Time API (JSR 310) 対応

[Java 8 Date & Time API (JSR 310)](https://jcp.org/en/jsr/detail?id=310) のオブジェクトは、現状の freemarker ではサポートされていません。つまり、Freemarker に LocalDateTime などの JSR 310 で導入された Date/Time 系のオブジェクトを渡しても Date/Time 処理用のフィルタなどを利用することができません。

freemarker-java-8 を利用すればできるようですが、このライブラリは maven central にリリースされておらず、使いづらいです。
https://github.com/amedia/freemarker-java-8

それほど多くないコード量で実現可能なので、以下のように設定するのが良いでしょう。

```java
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleDate;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
public class FreemarkerConfig {
    @Autowired
    void configurefreeMarkerConfigurer(FreeMarkerConfig configurer) {
        configurer.getConfiguration().setObjectWrapper(new CustomObjectWrapper());
    }

    private static class CustomObjectWrapper extends DefaultObjectWrapper {
        public CustomObjectWrapper() {
            super(freemarker.template.Configuration.VERSION_2_3_25);
        }

        @Override
        public TemplateModel wrap(Object obj) throws TemplateModelException {
            if (obj instanceof LocalDateTime) {
                Timestamp timestamp = Timestamp.valueOf((LocalDateTime) obj);
                return new SimpleDate(timestamp);
            } else if (obj instanceof LocalDate) {
                java.sql.Date date = java.sql.Date.valueOf((LocalDate) obj);
                return new SimpleDate(date);
            } else if (obj instanceof LocalTime) {
                Time time = Time.valueOf((LocalTime) obj);
                return new SimpleDate(time);
            } else {
                return super.wrap(obj);
            }
        }
    }

}
```

Note: ( https://issues.apache.org/jira/browse/FREEMARKER-35 そもそもコアで対応してほしいので issue 上げてみました。 )

## Wrapper を使う。

header/footer をちまちま設定するよりもマクロでラッパーしたほうが便利です。

`src/main/resources/templates/__wrapper.ftl` というファイル名で以下の内容を置きます。

```ftl
<#ftl strip_whitespace=true>
<#macro main subtitle="-">

<!doctype html>
<html>
<head>
    <title>${subtitle} - QND</title>
    <link rel="stylesheet" href="/webjars/bootstrap/3.3.5/dist/css/bootstrap.min.css">
</head>
<body>

    <#nested/>

</body>
</html>
</#macro>
```

利用側では以下のようにします。`<@wrapper.main>` から `</@wrapper.main>` までの間に書いた内容は `__wrapper.ftl` の `<#nested/>` の中に展開されます。

```ftl
<#import "/__wrapper.ftl" as wrapper>
<@wrapper.main>

<h1>List of items</h1>

...

</@wrapper.main>
```

### テンプレートファイルのパスを補完できるようにする

`src/main/resources/freemarker_implicit.ftl` を以下のように設定すると、`src/main/resources/templates/` をルートディレクトリとしてルート相対でテンプレートファイルのパスを IntelliJ で補完できるので便利です(`<#import "/__wrapper.ftl" as wrapper>` の `/__wrapper.ftl` の部分で補完がきくようになります)。

```ftl
[#ftl]
[#-- @implicitly included --]
[#-- @ftlroot "./templates" --]
```

## Freemarker cheat sheet

良く使う基本的な構文をいくつかここで例にあげます。他にも載せといたほうが便利なやつあれば随時追記しましょう。

```ftl

loop:

  <#list ["foo", "bar", "baz"] as x>
  ${x}
  </#list>

if:

  <#assign user="Big Joe">
  <#if user == "Big Joe">
    It is Big Joe
  </#if>

include:

  <#include "/footer/${company}.html">

switch:

  <#assign size="small">
  <#switch size>
    <#case "small">
       This will be processed if it is small
       <#break>
    <#case "medium">
       This will be processed if it is medium
       <#break>
    <#case "large">
       This will be processed if it is large
       <#break>
    <#default>
       This will be processed if it is neither
  </#switch>

文字列系の処理:

  HTML自動エスケープモードのときに、自動エスケープ対象外にする。
  ${"<b>hello</b>"?no_esc}

  文字列置換
  ${"this is a car acarus"?replace("car", "bulldozer")}

  前方一致
  ${"redirect"?starts_with("red")?c}

  文字列を含む？
  <#if "piceous"?contains("ice")>It contains "ice"</#if>

  後方一致
  <#if "piceous"?ends_with("ice")>It ends with "ice"</#if>

  文字列スライス
  ${"hoge"[0]}
  ${"hoge"[1..]}
  ${"hoge"[1..2]}
  ${"hoge"[1..*2]} ←　str[from..*maxLength]

  大文字に
  ${"GrEeN MoUsE"?upper_case}

  Capitalize
  ${"GreEN mouse"?capitalize}

  URL escape:
  ${'a/b c'?url}

  文字列の長さ:
  ${"GreEN mouse"?length}

数字系の処理:

  絶対値
  ${-5?abs}
  ${5?abs}

  整数化
  <#assign testlist=[
    0, 1, -1, 0.5, 1.5, -0.5,
    -1.5, 0.25, -0.25, 1.75, -1.75]>
  <#list testlist as result>
      ${result} ?floor=${result?floor} ?ceiling=${result?ceiling} ?round=${result?round}
  </#list>

  フォーマット
  ${3.14?string["0.#"]}

日時系の処理:

  現在の日時を得る
  ${.now}

  SimpleDateFormat のパターンでフォーマットする
  ${.now?string["yyyy-MM-dd(EEE) HH:mm"]}

boolean 系の処理:

  文字列化
  ${true?c}

  分岐しての文字列化
  ${true?string("yes", "no")}

リスト系の処理:

  join
  ${["red", "green", "blue"]?join(", ")}

  reverse
  ${["red", "green", "blue"]?reverse?join(", ")}

  size
  ${["red", "green", "blue"]?size}

  sort
  ${["red", "green", "blue"]?sort?join(", ")}

Map 系の処理:

  get
  ${{ "name": "mouse", "price": 50 }['name']}

  keys
  ${{ "name": "mouse", "price": 50 }?keys?join(", ")}

  values
  ${{ "name": "mouse", "price": 50 }?values?join(", ")}

ループ内変数

  <#list ['a', 'b', 'c'] as x>
    ${x?index}
    ${x?has_next?c}
    ${x?is_even_item?c}
    ${x?is_first?c}
    ${x?is_last?c}
    ${x?is_odd_item?c}
  </#list>

```

## バージョンについて

2.3.24-incubating で auto escape 機構が実装されました。
Web アプリケーションを実装する場合、auto escape 機能はぜひ利用したい機能です。
HTML escape 漏れから開放され、Web アプリケーションをセキュアに実装することが可能になります。

できるだけ 2.3.24-incubating 以後を利用し、auto escape 機能を利用しましょう。

なお `-incubating` の suffix は apache incubating だということを示しているだけです。
開発版であることを示しているわけではないので、安心してご利用ください。

その他、Escape 系のフィルタの security fix なども含まれている事があるので、最新版を利用することをおすすめします。

## spring.ftl について

spring.ftl は、便利なマクロが含まれているコードです。

### spring.ftl と auto escape

spring-webmvc に含まれている spring.ftl は auto escape に対応していません。
互換性の問題があるので、spring 4.x 系列では対応しないとのことです(ref. [SPR-14740](https://jira.spring.io/browse/SPR-14740))

現状では spring.ftl の内容をコピペして問題を修正して利用する必要があります。

spring 5.0 リリースタイミングで修正される見込みです。
  