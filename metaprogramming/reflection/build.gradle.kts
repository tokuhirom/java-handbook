plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
    testImplementation("com.google.guava:guava:33.4.0-jre")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
}

tasks.compileJava {
    options.compilerArgs.addAll(
        listOf(
            "-Werror",
            "-Xlint:all",
            "-Xlint:-processing", // Don't fail for annotations not claimed by annotation processors.
            "-Xlint:-serial", // Don't fail for serialVersionUID warnings.
            "-parameters", // Capture method parameter names in classfiles.
        ),
    )
}
