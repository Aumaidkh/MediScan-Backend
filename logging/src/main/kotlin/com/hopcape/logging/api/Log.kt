package com.hopcape.logging.api

/**
 * ## Log
 *
 * A data class that represents a log entry containing a message, a tag, and a status. This class is used to encapsulate
 * all relevant information about a log event, making it easy to pass around and process.
 *
 * ### Key Features:
 * - Encapsulates the log message, tag, and status in a single object.
 * - Provides default values for the tag (`"Unknown"`) and status (`Status.INFO`), ensuring ease of use.
 * - Includes an inner [Status] enum class to define the possible statuses for a log: SUCCESS, FAILURE, WARNING, and INFO.
 *
 * ### Usage:
 * The `Log` class is typically used in conjunction with logging mechanisms (e.g., [Logger] or [FormattedLogger]) to
 * represent individual log entries. It can be instantiated directly or through utility methods provided by logging
 * interfaces or classes.
 *
 * #### Example:
 * ```kotlin
 * fun main() {
 *     // Create a log entry with a success status
 *     val successLog = Log("Operation completed successfully", "API", Log.Status.SUCCESS)
 *     println(successLog) // Output: Log(message=Operation completed successfully, tag=API, status=SUCCESS)
 *
 *     // Create a log entry with default tag and status
 *     val defaultLog = Log("This is a default log")
 *     println(defaultLog) // Output: Log(message=This is a default log, tag=Unknown, status=INFO)
 *
 *     // Create a log entry with a custom status but default tag
 *     val warningLog = Log("Disk space is low", status = Log.Status.WARNING)
 *     println(warningLog) // Output: Log(message=Disk space is low, tag=Unknown, status=WARNING)
 * }
 * ```
 *
 * ### Properties:
 *
 * #### `val message: String`
 * The log message describing the event being logged.
 *
 * #### `val tag: String`
 * A tag to categorize the log (e.g., "API", "Database"). Defaults to `"Unknown"` if not provided.
 *
 * #### `val status: Status`
 * The status of the log, represented by the [Status] enum. Defaults to [Status.INFO] if not provided.
 *
 * ### Inner Enum Class: Status
 *
 * #### `enum class Status`
 * Defines the possible statuses for a log:
 * - **SUCCESS**: Indicates a successful operation or event.
 * - **FAILURE**: Indicates an error or failure.
 * - **WARNING**: Indicates a potential issue or warning.
 * - **INFO**: Indicates general informational messages.
 */
data class Log(
    val message: String,
    val tag: String = "Unknown",
    val status: Status = Status.INFO
) {
    /**
     * An enum class representing the possible statuses for a log.
     */
    enum class Status {
        SUCCESS,
        FAILURE,
        WARNING,
        INFO
    }
}