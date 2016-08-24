# IntelliJ について

弊社では 申請すればライセンスもらえるというところもあり IDE 人口が IntelliJ が最近は８割ぐらいいそうな感じです。

Java の IDE では他にも Eclipse や NetBeans などがありますが、特に、Java を今まで触っていなかった人には IntelliJ の利用をおすすめします。
IntelliJ は、Java の良くない記法などを黄色く表示して警告してくれるので、初心者には特におすすめです。

## 入れたほうが良いプラグイン

IntelliJ はプラグインで拡張可能ですが、以下のプラグインは特におすすめです。

### Save actions

保存時に何かできます。

ファイル保存時にOptimize Imports や Reformat Code できますのでチーム開発では有効にすることをおすすめします。
（設定しないと checkstyle に怒られがち)

### lombok

IntelliJ 単体では lombok に対応していません。lombok プラグインを導入したほうが良いです。

(なお、IntelliJ 使っていて lombok 依存に入れているはずなのに lombok きかない時は大体の場合、Enable Annotation Processors の指定が外れています。有効にする方法はこちらご覧ください　https://www.jetbrains.com/help/idea/2016.2/configuring-annotation-processing.html)

