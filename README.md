# java-testing-handbook

java に関する雑多なポエムです。
Java を始めたての頃にこういう情報を教えてくれる人がいれば助かったのにな、というのを書いてみました。

基本的に自社開発の web application のサーバーサイドを効率よく実装したいという観点にもとづいて書いているので、それ以外のパターンだと必ずしもマッチしないかもしれません。主に自社の人に「オレはこーしてるけど、他の人たちはどーしてるんすかね？」と問いかけるようなドキュメントになっています。それなら社内でのみ公開すればいいんだけど、まあもったいないから外に出してるってなもんですわ。自分がなんとなく持っている知識をはきだし、共有するとともに、他の人の持っている知識も吐き出してもらって、より効率的に開発したいという思いがあります。

気になることあれば、issues などでご指摘ください。p-r 等で追記していただいても構いません。

## ディレクトリ構成

src/ 以下がサンプルコード。build.gradle がサンプルコードの依存関係を書いてあるやつ。

## 目次

 * [ant](https://github.com/tokuhirom/java-handbook/blob/master/ant.md) - XML で書く Makefile
 * [maven central](https://github.com/tokuhirom/java-handbook/blob/master/maven-central.md) - ビルドツール
 * [gradle](https://github.com/tokuhirom/java-handbook/blob/master/gradle.md) - ビルドツール
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
