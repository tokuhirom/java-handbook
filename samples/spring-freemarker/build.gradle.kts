plugins {
    id("java")
    id("org.springframework.boot") version "4.0.3"
}

dependencies {
    implementation("com.google.guava:guava")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.webjars.npm:bootstrap")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
