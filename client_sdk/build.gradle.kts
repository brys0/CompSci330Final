plugins {
    id("java")
}

group = "org.uwgb.compsci330.client_sdk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(project(":common"))

    // Getter & setter generation
    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")

    // HTTP Client
    implementation("com.squareup.okhttp3:okhttp:5.3.2")

    // Source: https://mvnrepository.com/artifact/tools.jackson.core/jackson-core
    implementation("tools.jackson.core:jackson-core:3.1.0")
    // Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
    implementation("com.fasterxml.jackson.core:jackson-annotations:3.0-rc5")

}

tasks.test {
    useJUnitPlatform()
}