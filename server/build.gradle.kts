plugins {
	java
	id("org.springframework.boot") version "4.0.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "org.uwgb.compsci330"
version = "0.0.1-SNAPSHOT"
description = "API for Chat App written 100% in Java."

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

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.xerial:sqlite-jdbc:3.41.2.2")
	implementation("org.hibernate.orm:hibernate-community-dialects")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
// Source: https://mvnrepository.com/artifact/org.springframework.security/spring-security-core
//	implementation("org.springframework.security:spring-security-core:7.1.0-M2")
	//implementation("org.springframework.boot:spring-boot-starter-actuator")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
//	developmentOnly("org.springframework.boot:spring-boot-docker-compose")
	annotationProcessor("org.projectlombok:lombok")

	runtimeOnly("com.h2database:h2")


	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5") // for JSON parsing

	// Spring OpenAPI documentation
//	implementation("org.springdoc:springdoc-openapi-data-rest:1.8.0")
	implementation("org.springdoc:springdoc-openapi-starter-common:3.0.1")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.1")

	// Testing
	testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-webmvc-test")
	// Source: https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-test-autoconfigure
	testImplementation("org.springframework.boot:spring-boot-test-autoconfigure:4.0.3")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
