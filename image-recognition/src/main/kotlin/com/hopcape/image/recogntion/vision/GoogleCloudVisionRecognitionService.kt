package com.hopcape.image.recogntion.vision

import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.protobuf.ByteString
import com.hopcape.cache.api.Cache
import com.hopcape.image.recogntion.api.ImageRecognitionService
import com.hopcape.image.recogntion.utils.generateHashForImage
import com.hopcape.logging.api.Log
import com.hopcape.logging.api.Logger
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

const val GOOGLE_CLOUD_VISION = "GoogleCloudVisionRecognitionService"

/**
 * A service implementation of [ImageRecognitionService] that uses Google Cloud Vision API to recognize text or labels
 * from images. This class integrates with a caching mechanism ([Cache]) to optimize repeated image recognition requests.
 *
 * ### Key Features:
 * - Sends images to the Google Cloud Vision API for text detection.
 * - Extracts and processes detected text annotations into a structured format.
 * - Uses a cache to store and retrieve previously processed image results, improving performance.
 * - Filters out short or irrelevant text and ensures unique results.
 *
 * ### Example Usage:
 * ```kotlin
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
 * @property vision An instance of [ImageAnnotatorClient] used to interact with the Google Cloud Vision API.
 * @property cache An instance of [Cache] used to store and retrieve cached image recognition results.
 */
@Service
@Qualifier(GOOGLE_CLOUD_VISION)
internal class GoogleCloudVisionRecognitionService(
    /**
     * An instance of [ImageAnnotatorClient] used to interact with the Google Cloud Vision API.
     */
    private val vision: ImageAnnotatorClient,
    private val cache: Cache,
    private val logger: Logger
) : ImageRecognitionService {

    override fun recognizeImage(imageBytes: ByteArray): String {
        // Return cached result
        val cachedKey = generateHashForImage(imageBytes)
        cache.get(cachedKey)?.let {
            log(message = "Cache Hit: $it")
            return it
        }
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

        return detectedTexts.joinToString(separator = ",").also {
            // Update Cache
            cache.update(
                key = cachedKey,
                data = it
            )
            log(message = "Cache Miss: $it")
        } // Join results into a comma-separated string
    }

    /**
     * Logs a message with the specified status.
     *
     * @param message The message to log.
     * @param status The log status (default is [Log.Status.INFO]).
     */
    private fun log(
        message: String,
        status: Log.Status = Log.Status.INFO
    ) {
        logger.log(
            log = Log(
                message = message,
                tag = this::class.simpleName.toString(),
                status = status
            )
        )
    }
}