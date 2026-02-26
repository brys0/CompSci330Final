plugins {
    id("java")
    id("application")

    id("org.openjfx.javafxplugin") version ("0.1.0")
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
}

tasks.test {
    useJUnitPlatform()
}

javafx {
    version = "25.0.2"
    modules = listOf("javafx.graphics", "javafx.controls", "javafx.fxml")
}