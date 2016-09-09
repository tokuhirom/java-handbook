# slf4j

ロガーライブラリです。slf4j はロギングのインターフェースだけで、実際のログ出力は logback 等が行います。

## 他のロガーライブラリとの違い

正直、大差はないですが、slf4j が最近は人気っぽいので slf4j 使っとけばいいんじゃないですかぐらいの感じです。

 * log4j
 * log4j2
 * commons-logging
 * java.util.logging

 
## FAQ

### java.util.logging が標準だし最高なのでは？
 
java.util.logging は java 1.4 の時代に導入され、ロガー界を統一せしめ、世界に平和をもたらす存在だと信じられていました。
しかし、その微妙なインターフェースと実装により、世界の統一は失敗に終わっており、負の遺産と化しています。

現代に生きる我々にできることは、その存在を忘れることのみです。

### なんか slf4j がゴチャゴチャいってくるのですが 

以下のようなエラーメッセージが表示される場合、ログの出力を担当するクラスが存在していないことに問題があります。

    SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
    SLF4J: Defaulting to no-operation (NOP) logger implementation
    SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.

ログの出力は以下の２つしか利用することは無いでしょう。

#### slf4j-simple
 
slf4j-simple はその名の通り、シンプルなログ出力ライブラリです。
主に、slf4j 使っているクラスのテスト時に testCompile で依存にいれるのに利用します。

細かい設定はできませんが、入れるだけで STDERR にログを出力できます。

#### logback

ログの出力を XML/Groovy で細かく設定できます。
基本的にはこれを利用します。
