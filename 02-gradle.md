# gradle

gradle は ruby の gem コマンドや python の pip コマンドにあたるやつです。

## maven とどっちがいいの?

 * maven の方が IDE で補完がききやすいです。
 * maven が出力するエラーメッセージは意味不明であり、頭を抱えるほかありません
 * maven の機能を拡張するには plugin を書く必要がありますが、plugin をいったん local repository にデプロイしないと試せず、極めて苦痛です。
 * gradle は groovy で用意に拡張できます

## gradle の設定ファイル

基本的には build.gradle と settings.gradle の２種類を記述すればOKです。
build.gradle がメインの設定ファイルであり、settings.gradle はサブプロジェクトの include ができます。


## gradle とかでインストールできるライブラリをリリースしたいのですが

### 社内利用のライブラリの場合

弊社の場合では [nexus repository](https://www.sonatype.com/nexus-repository-sonatype) を運用していますので、社内で利用したいレポジトリはこちらにアップロードすればOKです。
社内レポジトリにリリースする場合には、合わせて source jar と doc jar もリリースすることをお忘れなく。

### Maven Central にリリースする

全世界にむけてリリースする場合、Maven central にリリースするのが良いでしょう。

リリース手順については tagomoris さんのエントリが詳しいのでこちら読むと良いでしょう。こちら読めばすぐにできます。

 * [はじめてのmaven central 公開](http://tagomoris.hatenablog.com/entry/20141028/1414485679)
 * [GradleでMaven Centralにライブラリを公開する](http://tagomoris.hatenablog.com/entry/2016/02/16/114226)

(jCenter に公開する方法もあるみたいですが、私はやったことがありません。)

### github に maven repo を構築する

実現可能ですが、おすすめしません。gradle/maven は取得元 repository を追加するとすべてのライブラリについて各レポジトリをスキャンすることになってしまい、全体的にビルド速度が低下します。そのため、野良レポジトリは嫌われています。

自分も過去に試したことがありますが、そのような理由から中止しました
