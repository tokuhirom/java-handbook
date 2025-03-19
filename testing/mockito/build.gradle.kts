plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:33.4.5-jre")

    testImplementation("org.mockito:mockito-core:5.16.1")
    testImplementation("org.mockito:mockito-junit-jupiter:5.16.1")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.12.1")
}

tasks.test {
    useJUnitPlatform()
}
