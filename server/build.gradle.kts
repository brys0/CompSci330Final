plugins {
	java
	id("org.springframework.boot") version "4.0.2"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.hibernate.orm") version "7.2.1.Final"
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
	// Springboot dependencies
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc") {
		exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
	}
	implementation("org.springframework.boot:spring-boot-starter-jetty")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.springframework.boot:spring-boot-starter-security")

	// Springboot OpenAPI annotation and generation
	// OpenAPI generation vuln: https://github.com/advisories/GHSA-72hv-8253-57qq
	implementation("org.springdoc:springdoc-openapi-starter-common:3.0.1")
//	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.1")

	// Database & helper dependencies
	implementation("org.xerial:sqlite-jdbc:3.41.2.2")
	implementation("org.hibernate.orm:hibernate-community-dialects")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

	// Getter & setter generation
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// JWT tokens
	implementation("io.jsonwebtoken:jjwt-api:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")

	// Testing
	testImplementation("org.junit.jupiter:junit-jupiter:6.0.2")

	// Testing Springboot
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-webmvc-test")
	testImplementation("org.springframework.boot:spring-boot-test-autoconfigure:4.0.3")

	// Testing DB
	testRuntimeOnly("com.h2database:h2")

	// Custom annotation processor (to warn against using internal api methods)
	annotationProcessor(project(":server:processor"))
	compileOnly(project(":server:annotation"))
}

tasks.withType<Test> {
	useJUnitPlatform()
}

hibernate {
	enhancement {
		enableLazyInitialization = true
		enableDirtyTracking = true
		enableAssociationManagement = true
		enableExtendedEnhancement = false
	}
}



