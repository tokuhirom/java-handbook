plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:33.4.8-jre")
    testImplementation("org.assertj:assertj-core:3.27.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.13.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.13.2")
}
