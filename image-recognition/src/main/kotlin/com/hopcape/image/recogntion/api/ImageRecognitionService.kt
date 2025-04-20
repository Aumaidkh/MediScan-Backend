package com.hopcape.image.recogntion.api

/**
 * A functional interface defining the contract for recognizing and extracting text or labels from an image. This
 * interface is designed to be implemented by various services that interact with image recognition models, APIs, or
 * other mechanisms to process images and extract meaningful information.
 *
 * The `recognizeImage` method takes an image in the form of a byte array and returns a string containing the recognized
 * text, labels, or other extracted information from the image.
 *
 * ### Key Features:
 * - Simplifies the integration of different image recognition mechanisms (e.g., AI models, external APIs, or local
 *   processing libraries).
 * - Provides a consistent interface for extracting information from images regardless of the underlying implementation.
 * - Enables dependency injection in Spring applications by allowing multiple implementations to be injected based on
 *   qualifiers or configurations.
 *
 * ### Example Usage:
 * ```kotlin
 * // Example: Using an ImageRecognitionService implementation in a Spring application
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
 * // "Cipla Paracetamol Tablets IP PARACIP-500 रासप 10, Cipla, Paracetamol, Tablets, PARACIP, 500, रासप"
 * ```
 *
 * ### Implementations:
 * - **Real Implementations**: Classes like [GoogleVisionImageRecognitionService] or [OpenRouterImageRecognitionService]
 *   that interact with external APIs or models to extract text or labels from images.
 * - **Mock Implementations**: Classes like [FakeImageRecognitionService] that provide predefined responses for testing
 *   and development purposes.
 *
 * ### Method:
 * - [recognizeImage]: Recognizes text or labels from the provided image bytes.
 */
fun interface ImageRecognitionService {

    /**
     * Recognizes text, labels, or other information from the provided image bytes.
     *
     * @param imageBytes A byte array representing the image to be processed. This could be the raw binary content of
     *                   an image file (e.g., JPEG, PNG).
     * @return A string containing the recognized text, labels, or other extracted information from the image.
     *         Implementations may return an empty string if no information can be extracted.
     *
     * @sample
     * ```kotlin
     * val service: ImageRecognitionService = GoogleVisionImageRecognitionService(apiKey, objectMapper, restTemplate)
     * val imageBytes = Files.readAllBytes(Paths.get("path/to/image.jpg"))
     * val labels = service.recognizeImage(imageBytes)
     *
     * println(labels)
     * // Output:
     * // "Cipla Paracetamol Tablets IP PARACIP-500 रासप 10, Cipla, Paracetamol, Tablets, PARACIP, 500, रासप"
     * ```
     */
    fun recognizeImage(imageBytes: ByteArray): String
}