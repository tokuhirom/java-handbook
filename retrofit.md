# retrofit - 宣言的な HTTP Client

[retrofit](http://square.github.io/retrofit/) は宣言的な HTTP Client です。
簡潔に記述できるため、業務で利用する http client としてはこれをお勧めしています。

内部的には okhttp を利用しています。

以下のように interface を定義します。

    public interface GitHubService {
      @GET("users/{user}/repos")
      Call<List<Repo>> listRepos(@Path("user") String user);
    }

この interface を元に、Retrofit に実装を作ってもらいます。

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .build();
    
    GitHubService service = retrofit.create(GitHubService.class);

あとは普通に呼べばいいだけです。

    Call<List<Repo>> repos = service.listRepos("octocat");

## Request Method

リクエストメソッドの指定は @GET, @HEAD, @POST, @DELETE, @PUT のアノテーションで行います。

例:

    @GET("users/list")

クエリパラメータを含める事もできます。

    @GET("users/list?sort=desc")

## URI 操作

URL を @Path で指定した変数で操作できます。

    @GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId);

クエリパラメータの追加もできます。

    @GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

Map での指定もできます。

    @GET("group/{id}/users")
    Call<List<User>> groupList(@Path("id") int groupId, @QueryMap Map<String, String> options);

## リクエストボディの指定

@Body で指定されたものは、Retrofit のインスタンスで指定されたコンバーターで変換されて　request body として送信されます。

    @POST("users/new")
    Call<User> createUser(@Body User user);

## x-www-form-urlencoded と multipart/form-data

`application/x-www-form-urlencoded` を送信したい場合は以下のようにしてください。

    @FormUrlEncoded
    @POST("user/edit")
    Call<User> updateUser(@Field("first_name") String first, @Field("last_name") String last);

`multipart/form-data` を送信したい場合には `@Multipart` を指定してください。

    @Multipart
    @PUT("user/photo")
    Call<User> updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);

## ヘッダの追加

特定のエンドポイントのみで利用されるヘッダは `@Headers` アノテーションで指定します。

    @Headers({
        "Accept: application/vnd.github.v3.full+json",
        "User-Agent: Retrofit-Sample-App"
    })
    @GET("users/{username}")
    Call<User> getUser(@Path("username") String username);

`@Header` アノテーションを利用すると、引数で渡す事もできます。

    @GET("user")
    Call<User> getUser(@Header("Authorization") String authorization)

認証ヘッダなどのすべてのエンドポイントで共通して送信するヘッダは、OkHttp のインターセプタを利用して送信してください。

## 大きいデータのダウンロード

@Streaming 指定をすれば、ストリーミングダウンロードが可能になります

    @Streaming
    @GET("/get/content)
    Call<ResponseBody> getContent();  


## 任意の URL へのアクセス

URL 全体をパラメータとして渡すこともできます

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);  


## 同期なの？非同期なの？

両方できます。利用シーンにあわせて使い分けてください。

## HOW IT WORKS?

java.lang.reflect.Proxy で interface に対するメソッド呼び出しをフックしています。
