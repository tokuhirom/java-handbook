# reflection

Java には reflection 機構があり、class, method などのメタデータを取得することが可能です。

reflection を実際に利用するシーンは少ないかもしれませんが、何をできるかを把握しておくことは Java で開発していく上では重要だと思います。

本稿では reflection でできる機能をいくつか紹介します。

## クラスリテラルからクラスオブジェクトを得る

`.class` とつけるとクラスリテラルからクラスオブジェクトを取得することができます。

```java
Class<ReflectionDemo> aClass = ReflectionDemo.class;
```

## インスタンスからクラスオブジェクトを得る

`getClass` メソッドでインスタンスからクラスオブジェクトを得ることができます。

```java
ReflectionDemo reflectionDemo = new ReflectionDemo();
Class<? extends ReflectionDemo> aClass = reflectionDemo.getClass();
```

## メソッドのリストを得る

以下のようなクラスがあるとき、

```java
public static class MyClass {
    private String privateField;
    public String publicField;

    @Ignore
    public String publicMethod(@NonNull String name) {
        return "Hello, " + name.toUpperCase();
    }

    private String secretMethod(@NonNull String name) {
        return "Hello, " + name.toUpperCase();
    }
}
```

以下のようにすると、メソッドの情報を得る事ができます。

```java
public void printMethods() {
    Class<MyClass> aClass = MyClass.class;
    System.out.println("--- getMethods() ---");
    for (Method method : aClass.getMethods()) {
        System.out.println(stringifyMethod(method));
    }
    System.out.println("--- getDeclaredMethods() ---");
    for (Method method : aClass.getDeclaredMethods()) {
        System.out.println(stringifyMethod(method));
    }
}

private String stringifyMethod(Method method) {
    return method.getDeclaringClass() + "." + method.getName() + "("
            + Stream.of(method.getParameters())
            .map(it -> it.getType().getSimpleName() + " " + it.getName())
            .collect(Collectors.joining(", "))
            + ")";
}
```

出力結果は以下のとおりです。`getMethods()` は親クラスのメソッドも含めた public メソッドが出力されます。

`getDeclaredMethods()` の方は、このクラスに定義されている private, public, package private, protected の 
メソッドが取得できます。

現実的には `getDeclaredMethods` のほうがよく使います。

```
--- getMethods() ---
class com.example.ReflectionDemo$MyClass.publicMethod(String name)
class java.lang.Object.wait(long arg0, int arg1)
class java.lang.Object.wait(long arg0)
class java.lang.Object.wait()
class java.lang.Object.equals(Object arg0)
class java.lang.Object.toString()
class java.lang.Object.hashCode()
class java.lang.Object.getClass()
class java.lang.Object.notify()
class java.lang.Object.notifyAll()
--- getDeclaredMethods() ---
class com.example.ReflectionDemo$MyClass.publicMethod(String name)
class com.example.ReflectionDemo$MyClass.secretMethod(String name)
```

パラメータの名前は通常、取得できませんが、java8 以後では javac に `-parameters` オプションを
つけてコンパイルされた場合には取得可能です。

## フィールドのリストを得る

以下のようなクラスがあるときに

```java
public class MyClass {
    private String privateField;
    public String publicField;

    @Ignore
    public String publicMethod(@NonNull String name) {
        return "Hello, " + name.toUpperCase();
    }

    private String secretMethod(@NonNull String name) {
        return "Hello, " + name.toUpperCase();
    }
}
```

以下のようなコードでフィールドの一覧を得ることができます。

```java
Class<MyClass> aClass = MyClass.class;
// 定義されている public フィールドの配列を得ます
System.out.println("--- getFields() ---");
for (Field field : aClass.getFields()) {
    System.out.println(field.getName());
}
// このクラスに定義されているフィールドの配列を得ます。
// public, protected, package private, private のものも得られます
System.out.println("--- getDeclaredFields() ---");
for (Field field : aClass.getDeclaredFields()) {
    System.out.println(field.getName());
}
```

出力結果は以下のとおりです。

```java
--- getFields() ---
publicField
--- getDeclaredFields() ---
privateField
publicField
```
