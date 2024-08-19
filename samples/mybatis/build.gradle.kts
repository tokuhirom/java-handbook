plugins {
    id("java")
}

dependencies {
    implementation("com.google.guava:guava")
    implementation("org.mybatis:mybatis-typehandlers-jsr310")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter")
    implementation("com.h2database:h2")

    testImplementation("org.mockito:mockito-core")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}
