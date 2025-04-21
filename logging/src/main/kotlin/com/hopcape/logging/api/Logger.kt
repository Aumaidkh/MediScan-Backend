@file:Suppress("DEPRECATION")

package com.hopcape.logging.api

import java.lang.Exception

/**
 * ## Logger
 *
 * An interface that defines the contract for logging messages with different statuses (e.g., success, failure, info).
 * This interface provides default implementations for convenience methods such as [logSuccess], [logError],
 * [logException], and [logInfo], which internally call the abstract [log] method.
 *
 * Implementations of this interface are responsible for defining the actual logging mechanism in the [log] method.
 *
 * ### Key Features:
 * - Provides default methods for logging messages with specific statuses: [logSuccess], [logError], [logException], and [logInfo].
 * - Supports tagging logs for better categorization and filtering.
 * - Ensures flexibility by allowing custom implementations of the [log] method.
 *
 * ### Usage:
 * To use this interface, you need to create a class that implements the [log] method to define the desired logging behavior.
 * Then, you can use the provided convenience methods to log messages with appropriate statuses.
 *
 * #### Example:
 * ```kotlin
 * class ConsoleLogger : Logger {
 *     override fun log(log: Log) {
 *         // Print the log message to the console with its status and tag.
 *         println("[${log.tag}] ${log.message} (${log.status})")
 *     }
 * }
 *
 * fun main() {
 *     val logger = ConsoleLogger()
 *
 *     // Log a success message
 *     logger.logSuccess("Operation completed successfully", "API")
 *
 *     // Log an error message
 *     logger.logError("Failed to fetch data", "API")
 *
 *     // Log an exception
 *     try {
 *         throw Exception("Something went wrong")
 *     } catch (e: Exception) {
 *         logger.logException(e, "API")
 *     }
 *
 *     // Log an informational message
 *     logger.logInfo("Starting data processing", "API")
 * }
 * ```
 *
 * ### Methods:
 *
 * #### `abstract fun log(log: Log)`
 * Logs the given [Log] object. Implementations of this method define the actual logging mechanism, such as writing
 * to a file, sending logs to a remote server, or printing to the console.
 *
 * @param log The [Log] object containing the message, tag, and status to be logged.
 *
 * #### `fun logSuccess(message: String, tag: String)`
 * Logs a success message with the specified tag.
 *
 * @param message The success message to log.
 * @param tag A tag to categorize the log (e.g., "API", "Database").
 *
 * #### `fun logError(message: String, tag: String)`
 * Logs an error message with the specified tag.
 *
 * @param message The error message to log.
 * @param tag A tag to categorize the log (e.g., "API", "Database").
 *
 * #### `fun logException(exception: Exception, tag: String)`
 * Logs an exception with the specified tag. The exception's message is used as the log message.
 *
 * @param exception The exception to log.
 * @param tag A tag to categorize the log (e.g., "API", "Database").
 *
 * #### `fun logInfo(message: String, tag: String)`
 * Logs an informational message with the specified tag.
 *
 * @param message The informational message to log.
 * @param tag A tag to categorize the log (e.g., "API", "Database").
 *
 * ### Notes:
 * - The [log] method is called internally by all the convenience logging methods ([logSuccess], [logError], etc.).
 * - The [Log.Status] enum defines the possible statuses for a log: SUCCESS, FAILURE, and INFO.
 */
interface Logger {
    /**
     * Logs the given [Log] object.
     *
     * @param log The [Log] object containing the message, tag, and status to be logged.
     */
    @Deprecated(
        message = "Use fine grained logging methods instead",
        replaceWith = ReplaceWith(
            "logSuccess,logFailure,logException,logInfo",
            imports = ["com.hopcape.logging.api"]
        ),
        level = DeprecationLevel.WARNING
    )
    fun log(log: Log)

    /**
     * Logs a success message with the specified tag.
     *
     * @param message The success message to log.
     * @param tag A tag to categorize the log (e.g., "API", "Database").
     */
    fun logSuccess(message: String, tag: String) {
        this.log(Log(message, tag, Log.Status.SUCCESS))
    }

    /**
     * Logs an error message with the specified tag.
     *
     * @param message The error message to log.
     * @param tag A tag to categorize the log (e.g., "API", "Database").
     */
    fun logError(message: String, tag: String) {
        this.log(Log(message, tag, Log.Status.FAILURE))
    }

    /**
     * Logs an exception with the specified tag. The exception's message is used as the log message.
     *
     * @param exception The exception to log.
     * @param tag A tag to categorize the log (e.g., "API", "Database").
     */
    fun logException(exception: Exception, tag: String) {
        this.log(Log(exception.message.toString(), tag, Log.Status.FAILURE))
    }

    /**
     * Logs an informational message with the specified tag.
     *
     * @param message The informational message to log.
     * @param tag A tag to categorize the log (e.g., "API", "Database").
     */
    fun logInfo(message: String, tag: String) {
        this.log(Log(message, tag, Log.Status.INFO))
    }

    /**
     * Logs an informational message with the specified tag.
     *
     * @param message The informational message to log.
     * @param tag A tag to categorize the log (e.g., "API", "Database").
     */
    fun logWarning(message: String, tag: String) {
        this.log(Log(message, tag, Log.Status.WARNING))
    }
}