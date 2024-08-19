plugins {
    id("java")
    id("org.springframework.boot") version "3.3.2"
}

dependencies {
    implementation("com.google.guava:guava")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
