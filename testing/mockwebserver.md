# mockwebserver

## SYNOPSIS

```java
public void test() throws Exception {
  // Create a MockWebServer. These are lean enough that you can create a new
  // instance for every unit test.
  MockWebServer server = new MockWebServer();

  // Schedule some responses.
  server.enqueue(new MockResponse().setBody("hello, world!"));
  server.enqueue(new MockResponse().setBody("sup, bra?"));
  server.enqueue(new MockResponse().setBody("yo dog"));

  // Start the server.
  server.start();

  // Ask the server for its URL. You'll need this to make HTTP requests.
  HttpUrl baseUrl = server.url("/v1/chat/");

  // Exercise your application code, which should make those HTTP requests.
  // Responses are returned in the same order that they are enqueued.
  Chat chat = new Chat(baseUrl);

  chat.loadMore();
  assertEquals("hello, world!", chat.messages());

  chat.loadMore();
  chat.loadMore();
  assertEquals(""
      + "hello, world!\n"
      + "sup, bra?\n"
      + "yo dog", chat.messages());

  // Optional: confirm that your app made the HTTP requests you were expecting.
  RecordedRequest request1 = server.takeRequest();
  assertEquals("/v1/chat/messages/", request1.getPath());
  assertNotNull(request1.getHeader("Authorization"));

  RecordedRequest request2 = server.takeRequest();
  assertEquals("/v1/chat/messages/2", request2.getPath());

  RecordedRequest request3 = server.takeRequest();
  assertEquals("/v1/chat/messages/3", request3.getPath());

  // Shut down the server. Instances cannot be reused.
  server.shutdown();
}
```

## DESCRIPTION

HTTP server を起動してモックサーバーとして起動させるためのライブラリです。
okhttp の一部として提供されています。

https://github.com/square/okhttp/tree/master/mockwebserver

## タイムアウトのシミュレーション

    response.setBodyDelay(7, TimeUnit.SECONDS)

でできます。

## 遅いレスポンスのシミュレーション

    response.throttleBody(1024, 1, TimeUnit.SECONDS);

でできます。最初の引数は bytes per second です。

## wiremock との比較

wiremock は static imports 前提になっており、補完が効かないためコピペ前提じゃないとコーディングが辛い面があります。

wiremock の場合、HTTP request に対してマッチャを記述していってそれに対して返ってくるレスポンスを処理していく感じになりますが、mockwebserver の場合にはそれが逆になります。

wiremock の考え方は「リクエストのパターンとレスポンスの対をモックサーバーに登録」なのに対し、mockwebserver は「レスポンスのデータを登録。リクエスト打ちまくった後でリクエストデータを assertion でチェック」です。

この差は大きくて、wiremock の場合は wiremock 特有のパターンマッチを覚えなくてはいけないが、mockwebserver はレスポンスは method の補完で入力できるし、リクエスト内容のバリデーションも assertj 等で簡単にできるので覚えることが少なくて良いのが良いです。

デメリットとして JSON を読むみたいな機能はないんで kotlin 使うか `IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("XXX"))` 的なことをするユーティリティメソッドを用意する必要があると思います。
