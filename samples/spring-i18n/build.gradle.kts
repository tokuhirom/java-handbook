plugins {
    id("java")
    id("org.springframework.boot") version "3.4.0"
}

dependencies {
    implementation("com.google.guava:guava")
    implementation("org.springframework.boot:spring-boot-starter-freemarker")
    implementation("org.springframework.boot:spring-boot-starter-web")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()

    // Configure test logging to show results in stdout
    testLogging {
        events("passed", "skipped", "failed")
        // Optionally include more detailed logs
        showStandardStreams = true
    }
}
