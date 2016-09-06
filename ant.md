# ant - XML で記述する Makefile

https://ant.apache.org/manual/

ant は XML で記述する Makefile　です。
なぜ XML で記述する必要があるのか？昔の Java エンジニアはなんでも XML でやるのが好きだったのです。
XML が先進的なテクノロジーだった時代が過去にはあったんですよ。。。

ant は利用せずにすめばそれが一番いいのですが、社内ツールの一部が ant に依存しているために、ごくごくまれに ant を利用する必要があります。

## ant のインストール

OSX の場合には以下。

    brew install ant

linux の場合には yum で入れればよいです。

## ant の基本的な記法

TBD

## 基本的なタスク

とりあえず普段自分で使っているタスクは以下のとおりです。

### echo

    <echo>Embed another:${line.separator}</echo>

メッセージを STDOUT に書きます。

https://ant.apache.org/manual/Tasks/echo.html

## exec タスク

    <exec executable="curl">
        <arg line="-d 'param1=value1&param2=value2' http://example.com/resource.cgi"/>
    </exec>

任意のコマンドを実行します。弊社では ant はデプロイツールの中で利用されていますので、主に curl 等を呼ぶときに利用します。
curl から hipchat にメッセージを送信する用途に利用しています。

## switch タスク

TBD