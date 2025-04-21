@file:Suppress("OVERRIDE_DEPRECATION")

package com.hopcape.logging.logger

import com.hopcape.logging.api.Log
import com.hopcape.logging.api.Logger
import org.springframework.stereotype.Service

/**
 * ## ConsoleLogger
 *
 * A concrete implementation of the [Logger] interface that logs messages to the console. This class is annotated with
 * [@Service] to make it a Spring-managed bean, allowing it to be easily injected into other components within a Spring
 * application.
 *
 * ### Key Features:
 * - Implements the [log] method from the [Logger] interface to print log messages to the console.
 * - Formats the log output to include the tag, status, and message for better readability.
 * - Designed to be used as a simple logging mechanism during development or debugging.
 *
 * ### Usage:
 * To use this logger, you can inject it into any Spring-managed component (e.g., a service or controller) and use the
 * provided logging methods such as [logSuccess], [logError], [logException], and [logInfo].
 *
 * #### Example:
 * ```kotlin
 * @Service
 * class MyService(private val logger: Logger) {
 *     fun performOperation() {
 *         try {
 *             // Simulate a successful operation
 *             logger.logSuccess("Operation completed successfully", "MyService")
 *
 *             // Simulate an error
 *             throw Exception("Something went wrong")
 *         } catch (e: Exception) {
 *             logger.logException(e, "MyService")
 *         }
 *     }
 * }
 *
 * fun main() {
 *     val logger = ConsoleLogger()
 *
 *     // Log an informational message
 *     logger.logInfo("Application started", "Main")
 *
 *     // Log a warning message
 *     logger.log(Log("Low disk space detected", "System", Log.Status.WARNING))
 * }
 * ```
 *
 * ### Methods:
 *
 * #### `override fun log(log: Log)`
 * Logs the given [Log] object by printing it to the console in a formatted manner. The output includes the tag,
 * status, and message for better readability.
 *
 * @param log The [Log] object containing the message, tag, and status to be logged.
 *
 * ### Notes:
 * - The `@Service` annotation ensures that this class is registered as a Spring-managed bean, making it easy to inject
 *   into other components.
 * - The `@file:Suppress("OVERRIDE_DEPRECATION")` annotation suppresses warnings related to overriding deprecated
 *   methods, if any exist in the parent interface.
 */
@Service
internal class ConsoleLogger : Logger {

    /**
     * Logs the given [Log] object by printing it to the console in a formatted manner.
     *
     * @param log The [Log] object containing the message, tag, and status to be logged.
     */
    override fun log(log: Log) {
        with(log) {
            println("[TAG: $tag, STATUS: ${status.name} -> $message]")
        }
    }
}