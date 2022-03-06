# junit5

junit 5 が出そうということで、junit5 のメモ。

http://junit.org/junit5/docs/current/user-guide/

なんかこのページによくまとまってるから改めて書く必要があんまないな。

## テストケースの作成

Java の世界では１個の実装に対して１個のテストクラスを実装するのが基本です。

IntelliJ の場合、実装テストを Cmd+Shift+T でテストケースをサッと作ることができます。

## build.gradle の書き方

以下のように書くと良い。

```
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'org.junit.platform:junit-platform-gradle-plugin:1.0.0-M2'
    }
}

group 'com.example'
version '1.0-SNAPSHOT'

apply plugin: 'java'
apply plugin: 'org.junit.platform.gradle.plugin'

ext.junitJupiterVersion  = '5.0.0-M2'

sourceCompatibility = 1.8

repositories {
    mavenCentral()
}

dependencies {
    testImplementation 'org.assertj:assertj-core:3.5.2'

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitJupiterVersion}")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:${junitJupiterVersion}")
}
```

## @Test

```
package com.example;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class HogeTest {
    @Test
    public void foo() {
        assertThat(false)
                .isTrue();
    }
}
```

@Test というアノテーションをつけたメソッドがテストケースとして実行されます。テストメソッドを書いたらば、カーソルをテストメソッドの中においておいて Cmd+Shift+R で実行できます。

## data driven tests

## conclusion
