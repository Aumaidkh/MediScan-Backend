// File: buildSrc/src/main/kotlin/Dependencies.kt (or CacheDependencies.kt)

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.addCache() {
    implementationProject(":cache")
}


fun DependencyHandlerScope.addLogger() {
    implementationProject(":logging")
}


fun DependencyHandlerScope.addImageRecognition() {
    implementationProject(":image-recognition")
}


fun DependencyHandlerScope.addNlp() {
    implementationProject(":clustering_algorithm")
}


fun DependencyHandlerScope.addCommon() {
    implementationProject(":common")
}


fun DependencyHandlerScope.addSecurity() {
    implementationProject(":security")
}


fun DependencyHandlerScope.addAuth() {
    implementationProject(":auth-service")
}


fun DependencyHandlerScope.spring() {
    kotlinTestImplementation("test")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

fun DependencyHandlerScope.springSecurity() {
    implementation("org.springframework.boot:spring-boot-starter-security")
}
fun DependencyHandlerScope.swaggerDocs() {
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")
}

fun DependencyHandlerScope.springValidation() {
    implementation("org.springframework.boot:spring-boot-starter-validation")
}

fun DependencyHandlerScope.mongodb(){
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
}


fun DependencyHandlerScope.cloudVision(){
    implementation("com.google.cloud:google-cloud-vision:3.17.0")
}


fun DependencyHandlerScope.aws(){
    implementPlatform("software.amazon.awssdk:bom:2.20.0")
    implementation("software.amazon.awssdk:rekognition")
}

fun DependencyHandlerScope.jwt(){
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")

    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
}

/**
 * Adds an implementation dependency to the project.
 *
 * @param dependencyNotation The dependency in the format group:name:version.
 */
private fun DependencyHandlerScope.implementation(dependencyNotation: String) {
    add("implementation", dependencyNotation)
}

private fun DependencyHandlerScope.kotlinTestImplementation(dependencyNotation: String) {
    add("testImplementation", kotlin(dependencyNotation))
}

private fun DependencyHandlerScope.implementationProject(dependencyNotation: String) {
    add("implementation",project(dependencyNotation))
}


private fun DependencyHandlerScope.implementPlatform(platformNotation: String) {
    add("implementation",platform(platformNotation))
}


private fun DependencyHandlerScope.compileOnly(dependencyNotation: String){
    add("compileOnly",dependencyNotation)
}

private fun DependencyHandlerScope.runtimeOnly(dependencyNotation: String){
    add("runtimeOnly",dependencyNotation)
}
