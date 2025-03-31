plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

    implementation("com.google.guava:guava:33.4.6-jre")
    implementation("org.mybatis:mybatis-typehandlers-jsr310:1.0.2")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")
    implementation("com.h2database:h2:2.3.232")

    testImplementation("org.mockito:mockito-core:5.16.1")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.1")
}
