# OpenJDK - Java の OSS 実装

http://openjdk.java.net

## Clone

* mercurial (hg) を入れる
* `hg clone http://hg.openjdk.java.net/jdk9/jdk9 jdk9`
* `cd !$; bash get_source.sh`
* ビルドはそのまま `bash configure; make` で ok

リポジトリの選び方は [この辺](http://www.slideshare.net/YujiKubota/openjdk-jjug/16) を参考に。

## パッチの出し方

* パッチを書く前に[バグデータベース](https://bugs.openjdk.java.net/)で検索しましょう。
* [OCA](http://www.oracle.com/technetwork/community/oca-486395.html) にサインして [ML](http://mail.openjdk.java.net/mailman/listinfo) にパッチを本文に貼り付けて投げる
    * ML の選び方は[ここら辺](http://www.slideshare.net/YujiKubota/openjdk-jjug/22)参考
