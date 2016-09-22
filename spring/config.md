# configuration file の記述方法

設定は `src/main/resources/config/application.yml` に記述します。
このファイルから各ライブラリごとの設定ファイルを読み込むように設定します。
大規模な案件の場合、各ライブラリごとの設定ファイルを分割したほうが管理しやすいです。

ファイルの中身は以下のようにします。各 profile ごとの挙動が設定されます。
spring の profile は Java 起動オプションの `-Dspring.profiles.active=release` や環境変数 `SPRING_PROFILE_ACTIVE` などで指定できます。

```yaml
---
# profile 無指定の場合は default profile になります。
# default profile の場合は local profile を適用します。
spring.profiles: default
spring.profiles.active: local
---
# local 開発環境用の設定です。appliation-logging-devel.yml 等を読み込みます。
spring.profiles: local
# spring.profile.include で設定ファイルを読み込むことができます。
# YAML のミラクルな記法で読みやすくしていますがそこはかとなく hack 臭さが漂います。
# TODO: ここを配列でかけたらいいなあ。という p-r を出すべきか……
spring.profiles.include: "\
  logging-devel,\
  mybatis-common,\
  freemarker-devel,\
  freemarker-common
"
---
# 本番環境用の設定です。
spring.profiles: release
spring.profiles.include: "\
  mybatis-common,\
  freemarker-release,\
  freemarker-common
"
```

後は、`src/main/resources/config/application-logging-devel.yml` のような各ファイルを設定していきます。たとえば application-logging-devel.yml の中身は以下のように設定します。

```yaml
# 開発時だけ有効にしておきたいデバッグ設定を記述します。
logging.level.org.springframework.web: DEBUG
logging.level.org.springframework.ui: DEBUG
logging.level.org.springframework.boot: DEBUG
```

このように設定ファイルを分割することには以下のようなメリットがあります。

 * 複数人で開発しているときに変更の衝突の可能性が減ります
 * 各ライブラリの設定ファイルを一発で開けるようになります
 * コードレビュー時に見やすいです
 * 他のプロジェクトの設定ファイルと見比べやすいです
 * 開発環境が複数ある場合でもコピペせずに済みます

最終的なファイルの配置は以下のようになります。
<img src="https://i.gyazo.com/6cbf5e06f8d1287ce8d7b4ad11b8c988.png">

## FAQ

### なぜ `src/main/resources/application.yml` に置かないの？

`src/main/resources/application.yml` に設定ファイルを設置することもできますが、リソースディレクトリのルートに置くと、散らかるので、`src/main/resources/config/` 以下に設置したほうが良い、と私は考えています。
