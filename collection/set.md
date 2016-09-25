# Set

java.util.Set は基本的な Java のコレクションの一つです。
Set は一つの要素は一つしか含みません。

## java.util.HashSet

要素を追加した順序を保持しないセットです。

```java
HashSet<Object> objects = new HashSet<>();
objects.add("A");
objects.add("X");
objects.add("P");
System.out.println(objects);
// => [P, A, X]
```

## java.util.LinkedHashSet

追加した順序を保持する Set です。

```java
LinkedHashSet<String> objects = new LinkedHashSet<>();
objects.add("A");
objects.add("X");
objects.add("P");
System.out.println(objects);
// => [A, X, P]
```

## java.util.TreeSet

要素を [natural order](https://docs.oracle.com/javase/7/docs/api/java/lang/Comparable.html) で保持する Set です。

```java
TreeSet<String> objects = new TreeSet<>();
objects.add("A");
objects.add("X");
objects.add("P");
System.out.println(objects);
```

## Guava の ImmutableSet

Google guava には Immutable (変更不能)な Set が含まれています。 

of で作ることができます。

```java
ImmutableSet<Object> build = ImmutableSet.of("A", "X", "P");
System.out.println(build.toString());
```

Builder が提供されているので Builder 経由で作成することもできます。

```java
ImmutableSet<Object> build = ImmutableSet.builder()
        .add("A")
        .add("X")
        .add("P")
        .build();
System.out.println(build.toString());
```

null を含めることはできません。null を渡した場合、NullPointerException が発生します。

## Stream からの Set 生成

`Collectors.toSet()` を利用すると、Stream から Set を生成できます。

```java
Set<String> build = Stream.of("A", "X", "P")
        .collect(Collectors.toSet());
System.out.println(build.getClass().toString());
System.out.println(build.toString());
```
