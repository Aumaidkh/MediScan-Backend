package com.hopcape.image.recogntion.fake

import com.hopcape.image.recogntion.api.ImageRecognitionService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

const val FAKE_RECOGNITION_SERVICE = "FakeRecognitionService"

/**
 * A mock implementation of [ImageRecognitionService] that simulates the behavior of a real image recognition service.
 * This service is intended for testing and development purposes, providing predefined text or labels as output without
 * requiring actual image processing or external APIs.
 *
 * The class is annotated with `@Service` and `@Qualifier(FAKE_RECOGNITION_SERVICE)` to enable Spring dependency injection.
 *
 * ### Key Features:
 * - Simulates the extraction of text or labels from an image.
 * - Returns a predefined string containing fake labels or text.
 * - Useful for unit testing, integration testing, and local development when external APIs or image processing are unavailable.
 *
 * ### Configuration Requirements:
 * - No external configuration is required since this is a mock service.
 *
 * ### Example Usage:
 * ```kotlin
 * // Example: Using the FakeRecognitionService in a Spring application
 * @Autowired
 * private lateinit var imageRecognitionService: ImageRecognitionService
 *
 * fun extractLabelsFromImage(imageBytes: ByteArray): String {
 *     return imageRecognitionService.recognizeImage(imageBytes)
 * }
 *
 * // Sample Input
 * val imageBytes = Files.readAllBytes(Paths.get("path/to/image.jpg"))
 * val labels = extractLabelsFromImage(imageBytes)
 *
 * // Sample Output
 * println(labels)
 * // Output:
 * // "Cipla Paracetamol Tablets IP PARACIP-500 रासप 10,Cipla,Paracetamol,Tablets,PARACIP,500,रासप"
 * ```
 *
 * ### Response Format:
 * The `recognizeImage` method always returns a predefined string containing fake labels or text. For example:
 * ```
 * Cipla Paracetamol Tablets IP PARACIP-500 रासप 10,Cipla,Paracetamol,Tablets,PARACIP,500,रासप
 * ```
 * This ensures consistent and predictable results for testing purposes.
 *
 * ### Use Cases:
 * - **Unit Testing**: Simulate image recognition responses without performing actual image processing.
 * - **Integration Testing**: Test the interaction between components without relying on external dependencies.
 * - **Local Development**: Enable developers to test functionality locally without configuring external services.
 *
 * @property output A predefined string containing fake labels or text to be returned by the `recognizeImage` method.
 *                  Default value is `"Cipla Paracetamol Tablets IP PARACIP-500 रासप 10,Cipla,Paracetamol,Tablets,PARACIP,500,रासप"`.
 *
 * @constructor Creates an instance of [FakeRecognitionService] with an optional custom output string.
 */
@Service
@Qualifier(FAKE_RECOGNITION_SERVICE)
internal class FakeRecognitionService(
    /**
     * A predefined string containing fake labels or text to be returned by the `recognizeImage` method.
     * Default value is `"Cipla Paracetamol Tablets IP PARACIP-500 रासप 10,Cipla,Paracetamol,Tablets,PARACIP,500,रासप"`.
     */
    private val output: String = "Cipla Paracetamol Tablets IP PARACIP-500 रासप 10,Cipla,Paracetamol,Tablets,PARACIP,500,रासप"
) : ImageRecognitionService {

    /**
     * Recognizes text or labels from the provided image bytes.
     *
     * This method ignores the input image bytes and always returns a predefined string containing fake labels or text.
     * It is intended for testing and development purposes only.
     *
     * @param imageBytes A byte array representing the image to be processed. Note: The input image bytes are ignored
     *                   in this mock implementation.
     * @return A string containing predefined fake labels or text.
     *
     * @sample
     * ```kotlin
     * val fakeService = FakeRecognitionService()
     * val imageBytes = Files.readAllBytes(Paths.get("path/to/image.jpg"))
     * val labels = fakeService.recognizeImage(imageBytes)
     *
     * println(labels)
     * // Output:
     * // "Cipla Paracetamol Tablets IP PARACIP-500 रासप 10,Cipla,Paracetamol,Tablets,PARACIP,500,रासप"
     * ```
     */
    override fun recognizeImage(imageBytes: ByteArray): String {
        return output
    }
}