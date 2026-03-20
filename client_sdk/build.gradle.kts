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

    implementation("com.fasterxml.jackson.core:jackson-core:2.18.0")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.18.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.0")

}

tasks.test {
    useJUnitPlatform()
}