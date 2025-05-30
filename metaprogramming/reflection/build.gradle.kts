plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.2")
    testImplementation("com.google.guava:guava:33.4.8-jre")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.13.0")
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
