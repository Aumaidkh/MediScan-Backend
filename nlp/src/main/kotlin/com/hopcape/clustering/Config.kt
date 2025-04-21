package com.hopcape.clustering

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
/**
 * A configuration class that defines beans for shared components in the application.
 * This class is annotated with `@Configuration` to indicate that it contains Spring configuration logic.
 *
 * ### Key Features:
 * - Provides a reusable [RestTemplate] bean for making HTTP requests in the application.
 * - Enables dependency injection of the [RestTemplate] instance into other components or services.
 *
 * ### Example Usage:
 * ```kotlin
 * // Example: Using the RestTemplate bean in a Spring service
 * @Service
 * class MyService(
 *     private val restTemplate: RestTemplate
 * ) {
 *     fun fetchData(url: String): String {
 *         return restTemplate.getForObject(url, String::class.java) ?: "No data found"
 *     }
 * }
 *
 * // Sample Input
 * val response = myService.fetchData("https://api.example.com/data")
 *
 * // Sample Output
 * println(response)
 * // Output:
 * // "{\"key\":\"value\"}"
 * ```
 *
 * ### Beans:
 * - `restTemplate`: Configures and provides an instance of [RestTemplate] for making HTTP requests.
 */
@Configuration
internal class Config {

    /**
     * Configures and provides a bean for [RestTemplate].
     *
     * This method creates and returns a new instance of [RestTemplate], which can be used to perform HTTP requests
     * such as GET, POST, PUT, and DELETE. The [RestTemplate] instance is automatically managed by the Spring container
     * and can be injected into other components via dependency injection.
     *
     * @return An instance of [RestTemplate] configured for use in the application.
     */
    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()
}