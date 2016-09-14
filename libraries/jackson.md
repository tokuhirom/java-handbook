# jackson - JSON やらを Java クラスにマッピングする

jackson は様々なフォーマットに対応したシリアライズ・デシリアライズライブラリです。
JSON 以外にも Avro, BSON, CBOR, CSV, Smile, Protobuf, XML そして YAML に対応しています。

JSON を Java クラスにマッピングする用途では Jackson が便利です。

## JSON のシリアライズ

シリアライズは `ObjectMapper#writeValueAsString()` 等でできます。

    @Value
    public static class Bar {
        String title;
    }

    @Test
    public void testSerialize() throws JsonProcessingException {
        Bar hoge = new Bar("hoge");

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(hoge));
    }

`writeValueAsBytes` でバイト列にシリアライズすることもできますし、`objectMapper.writeValue(outputStream, value)` のように、直接　jackson に outputStream に書き込ませる事もできます。

## JSON のデシリアライズ

`readValue` メソッドでデシリアライズできます。

    ObjectMapper objectMapper = new ObjectMapper();
    Foo foo = objectMapper.readValue("{\"foo\":3}", Foo.class);

`objectMapper.readValue(inputStream, klass)` のようにして、ストリームから直接読み込ませることも可能です。

## 未定義のフィールドを無視する。

Jackson はデフォルトでは未知のフィールドが JSON に含まれている場合に例外を投げます。この挙動は一般的に好まれないので、`FAIL_ON_UNKNOWN_PROPERTIES` を false に設定しておく必要があります。これを怠ると、連携先が出力する JSON にフィールドが追加されたら死ぬ、なんてことになります。。辛い。

きちんと以下のように設定しましょう。

```java
objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
```

`@JsonIgnoreProperties(ignoreUnknown = true)` を各クラスに付与することでも同様の効果が得られますが、すべてのマッピング対象クラスに付与すると煩雑になりますし、そもそもデフォルトの ObjectMapper の挙動がおかしいので、`FAIL_ON_UNKNOWN_PROPERTIES` を設定するほうをお勧めします。

```java
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyMappingClass {
}
```

## フィールドの名前をカスタマイズしたい

```java
@Data
public static class Entry {
    String title;
    @JsonProperty("blog_id")
    long blogId;
}
```

フィールドの名称そのものではない名前をつけたい場合。例えば Java で書いてないシステムからの移植の場合などには `@JsonProperty` をつけてカスタマイズする必要があります。

`@JsonProperty` をつけてカスタマイズした場合、その情報がコンストラクタに伝播されないという問題がありますので、`@lombok.Value` を現在のバージョンの jackson では利用できません(バージョン 2.5.3 時点)。

## JSON でプリモルフィズムしたい

JSON の中にいろいろな種類のオブジェクトが埋まってるパターン。

たとえば、以下のように、テキストメッセージと画像メッセージの２パターンが送られてくるパターン。

    {"type":"image", "url": "http://example.com/image"}

    {"type":"text", "body": "Hello!"}

このパターンの時には、以下のように `@JsonTypeInfo`, `@JsonSubTypes`, `@JsonTypeName` を利用してパースします。

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type",
            visible = true
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(ImageEntry.class),
            @JsonSubTypes.Type(TextEntry.class)
    })
    public interface Entry {
    }

    @JsonTypeName("image")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageEntry implements Entry {
        String url;
    }

    @JsonTypeName("text")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextEntry implements Entry {
        String body;
    }

これを利用して以下の様に利用します。

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    {
        Entry entry = objectMapper.readValue("{\"type\":\"image\", \"url\": \"http://example.com/image\"}", Entry.class);
        System.out.println(entry);
        // JacksonTypeTest.ImageEntry(url=http://example.com/image)
    }

    {
        Entry entry = objectMapper.readValue("{\"type\":\"text\", \"body\": \"Hello!\"}", Entry.class);
        System.out.println(entry);
        // JacksonTypeTest.TextEntry(body=Hello!)
    }

    {
        TextEntry textEntry = new TextEntry("Hello");
        String json = objectMapper.writeValueAsString(textEntry);
        System.out.println(json);
        // {"type":"text","body":"Hello"}
    }


## FAQ

### Gson と比べてどうですか？

Gson 使ったことないのでよくわからないです。

### cAmel のような名前のフィールド名で @JsonProperty を利用した場合にフィールドが二重に出力される

以下のように `cAmel` のようなフィールド名を指定して、しかも @JsonProperty を指定した場合に二重にフィールドが出力されてしまいます。

```java
@Test
public void test() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    Foo foo = new Foo("Hello", "world");
    log.info("{}", objectMapper.writeValueAsString(foo));
}

@Data
@AllArgsConstructor
@NoArgsConstructor
public static class Foo {
    @JsonProperty("ok")
    private String lower;

    @JsonProperty("ng")
    private String cAmel;
}
```

```
06:44:16.979 [main] INFO com.example.jackson.OjisanTest - {"camel":"world","ok":"Hello","ng":"world"}
```
これは、jackson が getter と field がそれぞれ別の jackson フィールドを指定していると認識しているからです。

このケースではクラス Foo は以下のように lombok がコンパイルされます。このとき、getCAmel と cAmel フィールドがペアであることを jackson は認識できていないようです(Jackson に問題があるのか lombok に問題があるのかは不明。追えばわかると思うけど追うモチベーションが無い。情報求む)。

```java
public static class Foo {
    @JsonProperty("ok")
    private String lower;
    @JsonProperty("ng")
    private String cAmel;

    public String getLower() {
        return this.lower;
    }

    public String getCAmel() {
        return this.cAmel;
    }

    public void setLower(String lower) {
        this.lower = lower;
    }

    public void setCAmel(String cAmel) {
        this.cAmel = cAmel;
    }
}
```

とにかく、以下のように明示的にゲッタ/セッター側にアノテーションを付与するようにすれば問題は解消されます。


```java
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bar {
        @JsonProperty("ok")
        private String lower;

        @Getter(onMethod = @__({@JsonProperty("ng")}))
        @Setter(onMethod = @__({@JsonProperty("ng")}))
        private String cAmel;
    }
```
