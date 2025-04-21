// File: buildSrc/src/main/kotlin/Dependencies.kt (or CacheDependencies.kt)

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project

/**
 * Adds the cache module as a dependency.
 */
fun DependencyHandlerScope.addCache() {
    implementationProject(":cache")
}

/**
 * Adds the logging module as a dependency.
 */
fun DependencyHandlerScope.addLogger() {
    implementationProject(":logging")
}

/**
 * Adds the image recognition module as a dependency.
 */
fun DependencyHandlerScope.addImageRecognition() {
    implementationProject(":image-recognition")
}

/**
 * Adds the NLP (Natural Language Processing) module as a dependency.
 */
fun DependencyHandlerScope.addNlp() {
    implementationProject(":nlp")
}

/**
 * Adds the common utilities module as a dependency.
 */
fun DependencyHandlerScope.addCommon() {
    implementationProject(":common")
}

/**
 * Adds the security module as a dependency.
 */
fun DependencyHandlerScope.addSecurity() {
    implementationProject(":security")
}

/**
 * Adds the authentication service module as a dependency.
 */
fun DependencyHandlerScope.addAuth() {
    implementationProject(":auth-service")
}

/**
 * Adds core Spring Boot dependencies commonly used across services.
 *
 * Includes:
 * - Spring Web (REST APIs)
 * - Spring WebFlux (Reactive APIs)
 * - Kotlin Reflection
 * - Kotlin Test dependencies
 */
fun DependencyHandlerScope.spring() {
    kotlinTestImplementation("test")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

/**
 * Adds Spring Security dependencies for securing applications.
 */
fun DependencyHandlerScope.springSecurity() {
    implementation("org.springframework.boot:spring-boot-starter-security")
}

/**
 * Adds Swagger/OpenAPI documentation dependencies.
 */
fun DependencyHandlerScope.swaggerDocs() {
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")
}

/**
 * Adds Spring Boot Validation starter dependencies for bean validation.
 */
fun DependencyHandlerScope.springValidation() {
    implementation("org.springframework.boot:spring-boot-starter-validation")
}

/**
 * Adds MongoDB and Reactive MongoDB starter dependencies.
 */
fun DependencyHandlerScope.mongodb() {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
}

/**
 * Adds Google Cloud Vision API dependency.
 */
fun DependencyHandlerScope.cloudVision() {
    implementation("com.google.cloud:google-cloud-vision:3.17.0")
}

/**
 * Adds AWS SDK dependencies using the BOM (Bill of Materials) approach.
 *
 * Includes:
 * - AWS SDK BOM for version management
 * - Rekognition service for image and video analysis
 */
fun DependencyHandlerScope.aws() {
    implementPlatform("software.amazon.awssdk:bom:2.20.0")
    implementation("software.amazon.awssdk:rekognition")
}

/**
 * Adds JWT (JSON Web Token) related dependencies for authentication and authorization.
 *
 * Includes:
 * - API module (compile only)
 * - Implementation and runtime modules
 */
fun DependencyHandlerScope.jwt() {
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
}

/**
 * Adds an implementation dependency.
 *
 * @param dependencyNotation The dependency string in format group:name:version.
 */
private fun DependencyHandlerScope.implementation(dependencyNotation: String) {
    add("implementation", dependencyNotation)
}

/**
 * Adds a Kotlin test dependency.
 *
 * @param dependencyNotation The test dependency string (e.g., "test").
 */
private fun DependencyHandlerScope.kotlinTestImplementation(dependencyNotation: String) {
    add("testImplementation", kotlin(dependencyNotation))
}

/**
 * Adds a project (module) as an implementation dependency.
 *
 * @param dependencyNotation The module path (e.g., ":common").
 */
private fun DependencyHandlerScope.implementationProject(dependencyNotation: String) {
    add("implementation", project(dependencyNotation))
}

/**
 * Adds a platform dependency (e.g., BOM for managing versions consistently).
 *
 * @param platformNotation The platform dependency string.
 */
private fun DependencyHandlerScope.implementPlatform(platformNotation: String) {
    add("implementation", platform(platformNotation))
}

/**
 * Adds a compileOnly dependency.
 *
 * @param dependencyNotation The dependency string.
 */
private fun DependencyHandlerScope.compileOnly(dependencyNotation: String) {
    add("compileOnly", dependencyNotation)
}

/**
 * Adds a runtimeOnly dependency.
 *
 * @param dependencyNotation The dependency string.
 */
private fun DependencyHandlerScope.runtimeOnly(dependencyNotation: String) {
    add("runtimeOnly", dependencyNotation)
}
