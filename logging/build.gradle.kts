plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.hopcape.logging"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.springframework.boot:spring-boot-starter-security")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(18)
}