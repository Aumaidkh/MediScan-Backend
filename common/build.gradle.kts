plugins {
    kotlin("jvm") version "2.1.10"
}

group = "${ProjectConfig.GROUP_NAME_PREFIX}.common"
version = ProjectConfig.VERSION_NAME

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(18)
}