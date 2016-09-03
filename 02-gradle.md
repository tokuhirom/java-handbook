# gradle

gradle は ruby の gem コマンドや python の pip コマンドにあたるやつです。

## maven とどっちがいいの?

 * maven の方が IDE で補完がききやすいです。
 * maven が出力するエラーメッセージは意味不明であり、頭を抱えるほかありません
 * maven の機能を拡張するには plugin を書く必要がありますが、plugin をいったん local repository にデプロイしないと試せず、極めて苦痛です。
 * gradle は groovy で用意に拡張できます

## gradle の設定ファイル

基本的には build.gradle と settings.gradle の２種類を記述すればOKです。
build.gradle がメインの設定ファイルであり、settings.gradle はサブプロジェクトの include ができるようです。
