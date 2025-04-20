package com.hopcape.image.recogntion.aws

import com.hopcape.clustering.api.MedicineDetailsPredictor
import com.hopcape.clustering.fake.FAKE_PREDICTOR
import com.hopcape.clustering.qwen.QWEN_PREDICTOR
import com.hopcape.image.recogntion.api.ImageRecognitionService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.rekognition.RekognitionClient
import software.amazon.awssdk.services.rekognition.model.DetectTextRequest
import software.amazon.awssdk.services.rekognition.model.Image
import software.amazon.awssdk.services.rekognition.model.TextDetection
import software.amazon.awssdk.services.rekognition.model.TextTypes

const val AWS_REKOGNITION = "AwsImageRekognitionService"
/**
 * Service implementation for image recognition using AWS Rekognition.
 *
 * This service interacts with the AWS Rekognition service to detect and recognize text
 * within an image. It leverages the RekognitionClient from the AWS SDK to send images
 * and receive detected text results.
 *
 * @property client An instance of RekognitionClient to communicate with AWS Rekognition.
 */
@Service
@Qualifier(AWS_REKOGNITION)
internal class AwsImageRekognitionService(
    private val client: RekognitionClient,
    @Qualifier(QWEN_PREDICTOR)
    private val predictor: MedicineDetailsPredictor
) : ImageRecognitionService {
    /**
     * Recognizes and extracts text from the given image using AWS Rekognition.
     *
     * This method processes the provided image data as a byte array,
     * sends it to the AWS Rekognition service, and retrieves the detected text content.
     *
     * @param imageBytes The image data as a byte array to be analyzed for text recognition.
     * @return A string containing the recognized text from the image, concatenated with commas.
     */
    override fun recognizeImage(imageBytes: ByteArray): String {
        // Convert the image bytes to SdkBytes
        val imageSdkBytes = SdkBytes.fromByteArray(imageBytes)

        // Create a DetectTextRequest
        val detectTextRequest = DetectTextRequest.builder()
            .image(Image.builder().bytes(imageSdkBytes).build())
            .build()

        // Call Amazon Rekognition to detect text in the image
        val detectTextResult = client.detectText(detectTextRequest)
        val detectedTexts = detectTextResult.textDetections()
        val labels = extractTop5ConfidentLabels(detectedTexts)
        // Extract and return the recognized text
        return predictor.predictByLabels(labels.joinToString(",")).name ?: "Unknown Medicine"
    }

    fun extractTop5ConfidentLabels(detectedTexts: List<TextDetection>): List<String> {
        return detectedTexts
            .asSequence()
            .filter { it.type() == TextTypes.WORD } // Only consider words (optional, remove if you want all types)
            .mapNotNull { detection ->
                val text = detection.detectedText() //?.replace(Regex("[^a-zA-Z0-9\\-]"), "")?.uppercase()
                val confidence = detection.confidence() // Assuming `confidence()` returns a numeric value
                if (!text.isNullOrBlank()) {
                    text to confidence
                } else null
            }
            .sortedByDescending { it.second } // Sort by confidence in descending order
            .take(5) // Take the top 5
            .map { it.first }
            .toList() // Extract only the text part
    }
}