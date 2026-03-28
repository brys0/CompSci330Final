
plugins {
    id("java")
    id("application")

    id("org.openjfx.javafxplugin") version ("0.1.0")
    id("com.gradleup.shadow") version "9.4.1"
}

group = "org.uwgb.compsci330.termfinal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(project(":client_sdk"))


    // Getter & setter generation
    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    version = "25.0.2"
    modules = listOf("javafx.graphics", "javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("org.uwgb.compsci330.frontend.Main")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.uwgb.compsci330.frontend.Main"
    }
}