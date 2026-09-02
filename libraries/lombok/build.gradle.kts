plugins {
    id("java")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.48")
    annotationProcessor("org.projectlombok:lombok:1.18.48")
    testCompileOnly("org.projectlombok:lombok:1.18.48")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.48")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.2")
    implementation("com.fasterxml.jackson.core:jackson-core:2.22.2")

    testImplementation("org.assertj:assertj-core:3.27.7")
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.1.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.1.3")
}
