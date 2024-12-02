plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

include("samples:guava")
include("samples:jackson")
include("samples:jmh")
include("samples:lombok")
include("samples:mybatis")
include("samples:reflection")
include("samples:memory-measurements")
include("samples:benchmarks")
include("samples:spring-freemarker")
include("samples:spring-i18n")

includeBuild("collection/collections")

includeBuild("database/mybatis")

includeBuild("libraries/guava")
includeBuild("libraries/jackson")
includeBuild("libraries/lombok")

includeBuild("metaprogramming/reflection")

includeBuild("testing/junit5")
includeBuild("testing/mockito")
