plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:33.5.0-jre")
    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.2")
}
