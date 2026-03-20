plugins {
    id("java")
}

group = "org.uwgb.compsci330.common"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation("org.jetbrains:annotations:15.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Getter & setter generation
    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")

    // Source: https://mvnrepository.com/artifact/tools.jackson.core/jackson-core
    implementation("tools.jackson.core:jackson-core:3.1.0")
    // Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
    implementation("com.fasterxml.jackson.core:jackson-annotations:3.0-rc5")

    // Source: https://mvnrepository.com/artifact/tools.jackson.core/jackson-databind
    implementation("tools.jackson.core:jackson-databind:3.1.0")
}

tasks.test {
    useJUnitPlatform()
}