plugins {
    id("java")
    id("java-library")
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

    api(project(":common"))

    // Getter & setter generation
    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")

    // HTTP Client
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    implementation("com.neovisionaries:nv-websocket-client:2.14")

    // Logger
    implementation("ch.qos.logback:logback-classic:1.5.32")
    implementation("org.slf4j:slf4j-api:2.0.17")
}

tasks.test {
    useJUnitPlatform()
}