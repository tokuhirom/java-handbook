# webjars

npm のファイルを jar でまとめたものが配布されています。
これを利用すると、twitter bootstrap 等を build.gradle に依存追加するだけで使えます。
便利〜

```groovy
dependencies {
  // Twitter bootstrap です。
  implementation 'org.webjars.npm:bootstrap:4.4.1'
}
```

## 利用方法

http://webjars.org/ から利用したい jar を選んで build.gradle に追記します。

あとは以下のようにテンプレートファイルから参照するだけで OK です。
```html
<link rel="stylesheet" href="/webjars/bootstrap/3.3.5/dist/css/bootstrap.min.css">
```

IntelliJ IDEA 上での補完も効きますので最高便利です。

## spring mvc の設定方法

[WebMvcAutoConfiguration](https://github.com/spring-projects/spring-boot/blob/master/spring-boot-autoconfigure/src/main/java/org/springframework/boot/autoconfigure/web/WebMvcAutoConfiguration.java) が自動で設定してくれるので、設定する必要はありません。

### なぜか webjars が効かない場合

WebMvcConfigurer に `@EnableWebMvc` が指定されているとデフォルトの設定が読み込まれなくなり、`/webjars/**` に対するパスのマッピングもされません。`@EnableWebMvc` が混在してないか確認してください。

## SEE ALSO

 * [Spring MVC(+Spring Boot)上での静的リソースへのアクセスを理解する](http://qiita.com/kazuki43zoo/items/e12a72d4ac4de418ee37)
