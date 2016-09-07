# java-testing-handbook

java に関する雑多なポエムです。
Java を始めたての頃にこういう情報を教えてくれる人がいれば助かったのにな、というのを書いてみました。

気になることあれば、issues などでご指摘ください

## ディレクトリ構成

src/ 以下がサンプルコード。build.gradle がサンプルコードの依存関係を書いてあるやつ。

## 目次

 * [ant](https://github.com/tokuhirom/java-handbook/blob/master/ant.md) - XML で書く Makefile
 * [maven central](https://github.com/tokuhirom/java-handbook/blob/master/maven-central.md) - ビルドツール
 * [gradle](https://github.com/tokuhirom/java-handbook/blob/master/gradle.md) - ビルドツール
 * Libraries
   * [guava](https://github.com/tokuhirom/java-handbook/blob/master/guava.md) - コンテナユーティリティー、オンメモリキャッシュ
   * [retrofit](https://github.com/tokuhirom/java-handbook/blob/master/retrofit.md) - 宣言的な HTTP Client
   * logback - TBD
   * lombok - TBD
   * slf4j - TBD
 * Testing
   * [mockito](https://github.com/tokuhirom/java-handbook/blob/master/testing/mockito.md) - モッキング
   * [mockwebserver](https://github.com/tokuhirom/java-handbook/blob/master/testing/mockwebserver.md) - シンプルなモック HTTP サーバー

## 書きたいと思っていて書いてないこと

 * jackson のこと
   * static class
 * mockito
   * argumentcaptor
