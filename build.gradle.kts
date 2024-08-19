plugins {
    id("org.springframework.boot") version "3.3.2" apply false
    id("io.spring.dependency-management") version "1.1.6"
    id("java")
    id("idea")
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

description = "java handbook sample code"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

allprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Xlint:deprecation", "-parameters"))
        options.encoding = "UTF-8"
    }

    val okhttp3Version by extra("4.12.0")

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }

        dependencies {
            dependency("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.3")
            dependency("org.mybatis:mybatis-typehandlers-jsr310:1.0.2")
            dependency("org.webjars.npm:bootstrap:5.3.3")
            dependency("com.squareup.okhttp3:mockwebserver:$okhttp3Version")
            dependency("com.github.jbellis:jamm:0.3.3")
            dependency("com.google.guava:guava:33.3.0-jre")
        }
    }
}

subprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    dependencies {
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        testCompileOnly("org.projectlombok:lombok")
        testAnnotationProcessor("org.projectlombok:lombok")
    }
}

configurations {
    implementation {
        exclude(module = "commons-logging")
        exclude(group = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}
