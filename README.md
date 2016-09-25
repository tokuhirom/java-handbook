# java-handbook

java に関する雑多なポエムです。
Java を始めたての頃にこういう情報を教えてくれる人がいれば助かったのにな、というのを書いて置いておくところです。

基本的に自社開発の web application のサーバーサイドを効率よく実装したいという人たちがメインで書いているので、それ以外のパターンだと必ずしもマッチしないかもしれません。一般的に役立つようになってる方がベターだと思うので、それはそのケースだけだろ〜　みたいなのアレば issue or pr plz.

気になることあれば、issues などでご指摘ください。p-r 等で追記したり新規で書いていただいても構いません。

## 古いバージョンに関する説明について

古いバージョンでは利用できない機能については「このバージョンより新しくしてください」という解説は入れますが、古いバージョンでのやり方についての説明などは入れない方針です（入れるとごちゃついてわかりにくくなるので)。

## ディレクトリ構成

src/ 以下がサンプルコード。build.gradle がサンプルコードの依存関係を書いてあるやつ。

## 目次

 * [ant](https://github.com/tokuhirom/java-handbook/blob/master/ant.md) - XML で書く Makefile
 * [maven central](https://github.com/tokuhirom/java-handbook/blob/master/maven-central.md) - ビルドツール
 * [gradle](https://github.com/tokuhirom/java-handbook/blob/master/gradle.md) - ビルドツール
 * Collections
   * [List](https://github.com/tokuhirom/java-handbook/blob/master/collection/list.md)
   * [Map](https://github.com/tokuhirom/java-handbook/blob/master/collection/map.md)
   * [Set](https://github.com/tokuhirom/java-handbook/blob/master/collection/set.md)
 * Libraries
   * [guava](https://github.com/tokuhirom/java-handbook/blob/master/libraries/guava.md) - コンテナユーティリティー、オンメモリキャッシュ
   * [retrofit](https://github.com/tokuhirom/java-handbook/blob/master/libraries/retrofit.md) - 宣言的な HTTP Client
   * [lombok](https://github.com/tokuhirom/java-handbook/blob/master/libraries/lombok.md) - ボイラープレートコードの生成
   * [slf4j](https://github.com/tokuhirom/java-handbook/blob/master/libraries/slf4j.md) - ロガー
   * logback - TBD
 * Testing
   * [mockito](https://github.com/tokuhirom/java-handbook/blob/master/testing/mockito.md) - モッキング
   * [mockwebserver](https://github.com/tokuhirom/java-handbook/blob/master/testing/mockwebserver.md) - シンプルなモック HTTP サーバー
 * Metaprogramming
   * reflection
   * java.lang.reflection.Proxy
   * Annotation Processing
   * AOP

## 書きたいと思っていて書いてないこと

 * jackson のこと
   * static class
 * mockito
   * argumentcaptor
 * サロゲートペアの話題
 * i18n について
 * MockMVC

## THANKS TO

以下の方々からレビューコメントを頂いて反映しています。

 * @kmizu
 * @gakuzzzz
 * @hishidama
