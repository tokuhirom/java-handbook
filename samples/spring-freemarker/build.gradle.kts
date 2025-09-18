plugins {
    id("java")
    id("org.springframework.boot") version "3.5.6"
}

dependencies {
    implementation("com.google.guava:guava")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.webjars.npm:bootstrap")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
