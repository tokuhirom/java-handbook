# visualvm

JVM プロセスの内部状態を見るためには visualvm が便利です。

## 起動

VisualVM は jdk に添付されており、たとえば OSX では `/Library/Java/JavaVirtualMachines/jdk1.8.0_77.jdk/Contents/Home/bin/jvisualvm` にあったりします。 

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

## FAQ

### Java Mission Control とどっちがいいの?

Java Mission Control でも同じ事ができます。
両方使ってみて使い勝手が良い方を利用するのが良いでしょう。
