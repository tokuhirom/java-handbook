plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:33.3.1-jre")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.3")
}
