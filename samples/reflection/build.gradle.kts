plugins {
    id("java")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testImplementation("com.google.guava:guava")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}

tasks.compileJava {
    options.compilerArgs.addAll(listOf(
            "-Werror",
            "-Xlint:all",
            "-Xlint:-processing", // Don't fail for annotations not claimed by annotation processors.
            "-Xlint:-serial", // Don't fail for serialVersionUID warnings.
            "-parameters" // Capture method parameter names in classfiles.
    ))
}
