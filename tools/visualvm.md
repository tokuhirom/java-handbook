# visualvm

JVM プロセスの内部状態を見るためには visualvm が便利です。

## インストール

https://visualvm.github.io/ からダウンロードしてインストールします。

## 起動

VisualVM の起動にはコマンドラインで jvisualvm と打って起動します。

起動すると以下のような画面が出て、ローカルのプロセスが選択できる状態になっています。

![](https://github.com/tokuhirom/java-handbook/blob/master/tools/_assets/visualvm-top.png?raw=true)

Overview タブを選択すると、プロセスの概要がわかります。

![](https://github.com/tokuhirom/java-handbook/blob/master/tools/_assets/visualvm-overview.png?raw=true)

CPU やヒープの状態もグラフでわかりやすく見ることができます。
CPU の使用率や GC の頻度などをざっくりと把握するのに便利です。

![](https://github.com/tokuhirom/java-handbook/blob/master/tools/_assets/visualvm-monitor.png?raw=true)

スレッドの様子もわかります。

![](https://github.com/tokuhirom/java-handbook/blob/master/tools/_assets/visualvm-threads.png?raw=true)

CPU/Memory サンプラもあります。
ざっくりとパフォーマンスボトルネックを把握するのに便利です。

![](https://github.com/tokuhirom/java-handbook/blob/master/tools/_assets/visualvm-sampler.png?raw=true)

## MBeans プラグイン

visualvm は素のままでも便利なのですが、MBeans プラグイン便利が極めて便利です。
MBeans プラグインをいれると JMX の MBeans 情報を取得することができて便利です。

Menu から Tools → Plugins を選んで、以下の画面からインストールしましょう。 

![](https://github.com/tokuhirom/java-handbook/blob/master/tools/_assets/install-mbeans-plugin.png?raw=true)

インストールすると以下のように MBeans にアクセスすることができるようになります。

![](https://github.com/tokuhirom/java-handbook/blob/master/tools/_assets/visualvm-mbeans.png?raw=true)

## Remote 接続

JMX のリモート接続を有効にしているサーバーに対して簡単に visualvm で接続できます。

以下のように "File -> Add JMX Connection" を選んで、JMX 接続を追加します。

[![https://gyazo.com/235aa0c66cd3db43a9c57433a6778a41](https://i.gyazo.com/235aa0c66cd3db43a9c57433a6778a41.gif)](https://gyazo.com/235aa0c66cd3db43a9c57433a6778a41)

あとはローカルのサーバーと同様に接続できます。

[![https://gyazo.com/0eedef0f29f56287d3621ea9df76e008](https://i.gyazo.com/0eedef0f29f56287d3621ea9df76e008.gif)](https://gyazo.com/0eedef0f29f56287d3621ea9df76e008)

## FAQ

### Java Mission Control とどっちがいいの?

Java Mission Control でも同じ事ができます。Flight Recorder なども利用できて便利なので、必要でアレば利用するといいでしょう。

### visualvm だとリモート接続に jmx と jstatd が選べるけどどちらを選んだらいいの？

jmx が良いです。

[jstatd](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstatd.html) は experimental だからです。

> Monitors Java Virtual Machines (JVMs) and enables remote monitoring tools to attach to JVMs. This command is experimental and unsupported.
  
