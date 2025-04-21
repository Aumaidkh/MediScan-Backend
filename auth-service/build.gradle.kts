plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.hopcape.auth"
version = ProjectConfig.VERSION_NAME

repositories {
    mavenCentral()
}

dependencies {
    spring()
    springSecurity()
    mongodb()
    addSecurity()
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(18)
}