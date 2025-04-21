package com.hopcape.logging.logger

import com.hopcape.logging.api.Log
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream


class ConsoleLoggerTest {
    private lateinit var consoleLogger: ConsoleLogger
    private lateinit var outputStream: ByteArrayOutputStream
    private val originalOut = System.out

    @BeforeEach
    fun setUp() {
        consoleLogger = ConsoleLogger()
        outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream)) // Redirect standard output to capture logs
    }

    @AfterEach
    fun tearDown() {
        System.setOut(originalOut) // Restore standard output
    }

    @Test
    fun `should log success message`() {
        // Arrange
        val log = Log(message = "Operation succeeded", tag = "TEST", status = Log.Status.SUCCESS)

        // Act
        consoleLogger.log(log)

        // Assert
        val expectedOutput = "[TAG: TEST, STATUS: SUCCESS -> Operation succeeded]"
        assertEquals(expectedOutput, outputStream.toString().trim())
    }

    @Test
    fun `should log error message`() {
        // Arrange
        val log = Log(message = "Operation failed", tag = "TEST", status = Log.Status.FAILURE)

        // Act
        consoleLogger.log(log)

        // Assert
        val expectedOutput = "[TAG: TEST, STATUS: FAILURE -> Operation failed]"
        assertEquals(expectedOutput, outputStream.toString().trim())
    }

    @Test
    fun `should log warning message`() {
        // Arrange
        val log = Log(message = "Disk space low", tag = "SYSTEM", status = Log.Status.WARNING)

        // Act
        consoleLogger.log(log)

        // Assert
        val expectedOutput = "[TAG: SYSTEM, STATUS: WARNING -> Disk space low]"
        assertEquals(expectedOutput, outputStream.toString().trim())
    }

    @Test
    fun `should log info message`() {
        // Arrange
        val log = Log(message = "Application started", tag = "MAIN", status = Log.Status.INFO)

        // Act
        consoleLogger.log(log)

        // Assert
        val expectedOutput = "[TAG: MAIN, STATUS: INFO -> Application started]"
        assertEquals(expectedOutput, outputStream.toString().trim())
    }

    @Test
    fun `should use default tag and status when not provided`() {
        // Arrange
        val log = Log(message = "Default log")

        // Act
        consoleLogger.log(log)

        // Assert
        val expectedOutput = "[TAG: Unknown, STATUS: INFO -> Default log]"
        assertEquals(expectedOutput, outputStream.toString().trim())
    }
}