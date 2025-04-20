package com.hopcape.image.recogntion.vision

import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.protobuf.ByteString
import com.hopcape.image.recogntion.api.ImageRecognitionService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

const val GOOGLE_CLOUD_VISION = "GoogleCloudVisionRecognitionService"
/**
 * A service implementation of [ImageRecognitionService] that uses Google Cloud Vision API to recognize text or labels
 * from an image. This service processes images using the Google Cloud Vision library and extracts meaningful text or
 * annotations for further use.
 *
 * The class is annotated with `@Service` and `@Qualifier(GOOGLE_CLOUD_VISION)` to enable Spring dependency injection.
 *
 * ### Key Features:
 * - Sends an image to the Google Cloud Vision API for text detection.
 * - Extracts and processes detected text annotations into a structured format.
 * - Filters out short or irrelevant text and ensures unique results.
 * - Returns a comma-separated string of detected labels or text for further processing.
 *
 * ### Configuration Requirements:
 * - Requires an active Google Cloud project with the Vision API enabled.
 * - Requires valid credentials (e.g., service account key) configured for the `ImageAnnotatorClient`.
 *
 * ### Example Usage:
 * ```kotlin
 * // Example: Using the GoogleCloudVisionRecognitionService in a Spring application
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
 * The `recognizeImage` method returns a comma-separated string of detected text or labels. For example:
 * ```
 * Cipla Paracetamol Tablets IP PARACIP-500 रासप 10,Cipla,Paracetamol,Tablets,PARACIP,500,रासप
 * ```
 * Each label or text is filtered to ensure it is meaningful (e.g., longer than 2 characters) and unique.
 *
 * ### Error Handling:
 * - If the Vision API fails to detect any text or annotations, the method returns an empty string.
 * - Errors during API calls are not explicitly handled in this implementation but can be logged or propagated as needed.
 *
 * @property vision An instance of [ImageAnnotatorClient] used to interact with the Google Cloud Vision API.
 *
 * @constructor Creates an instance of [GoogleCloudVisionRecognitionService].
 */
@Service
@Qualifier(GOOGLE_CLOUD_VISION)
internal class GoogleCloudVisionRecognitionService(
    /**
     * An instance of [ImageAnnotatorClient] used to interact with the Google Cloud Vision API.
     */
    private val vision: ImageAnnotatorClient
) : ImageRecognitionService {

    /**
     * Recognizes text or labels from the provided image bytes using the Google Cloud Vision API.
     *
     * This method sends the image to the Vision API for text detection, processes the response to extract meaningful
     * text annotations, and returns them as a comma-separated string.
     *
     * @param imageBytes A byte array representing the image to be processed. This could be the raw binary content of
     *                   an image file (e.g., JPEG, PNG).
     * @return A comma-separated string containing the detected text or labels. Returns an empty string if no text is
     *         detected or all detected text is filtered out.
     *
     * @sample
     * ```kotlin
     * val visionClient = ImageAnnotatorClient.create()
     * val googleVisionService = GoogleCloudVisionRecognitionService(visionClient)
     * val imageBytes = Files.readAllBytes(Paths.get("path/to/image.jpg"))
     * val labels = googleVisionService.recognizeImage(imageBytes)
     *
     * println(labels)
     * // Output:
     * // "Cipla Paracetamol Tablets IP PARACIP-500 रासप 10,Cipla,Paracetamol,Tablets,PARACIP,500,रासप"
     * ```
     */
    override fun recognizeImage(imageBytes: ByteArray): String {
        val image = Image.newBuilder().setContent(ByteString.copyFrom(imageBytes)).build()
        val request = AnnotateImageRequest.newBuilder()
            .addFeatures(Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION))
            .setImage(image)
            .build()

        val response = vision.batchAnnotateImages(listOf(request))

        val detectedTexts: List<String> = response.responsesList
            .asSequence()
            .flatMap { it.textAnnotationsList }
            .filter { it.description.length > 2 } // Filter out short or irrelevant text
            .map { it.description.replace("\n", " ") } // Replace newlines with spaces
            .filter { it.isNotBlank() } // Remove blank entries
            .distinct() // Ensure unique results
            .toList()

        return detectedTexts.joinToString(separator = ",") // Join results into a comma-separated string
    }
}