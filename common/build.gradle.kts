plugins {
    id("java")
    id("java-library")
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

    // Source: https://mvnrepository.com/artifact/tools.jackson/jackson-bom
    api(platform("tools.jackson:jackson-bom:3.1.0"))

    api("tools.jackson.core:jackson-databind")
    api("com.fasterxml.jackson.core:jackson-annotations")
    api("tools.jackson.core:jackson-core")
}

tasks.test {
    useJUnitPlatform()
}