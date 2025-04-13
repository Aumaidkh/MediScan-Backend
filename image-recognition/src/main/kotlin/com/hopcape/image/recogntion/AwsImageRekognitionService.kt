package com.hopcape.image.recogntion

import org.springframework.stereotype.Service
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.rekognition.RekognitionClient
import software.amazon.awssdk.services.rekognition.model.DetectTextRequest
import software.amazon.awssdk.services.rekognition.model.Image

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
internal class AwsImageRekognitionService(
    private val client: RekognitionClient
): ImageRecognitionService {
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

        // Extract and return the recognized text
        return detectedTexts.joinToString(", ") { it.detectedText() }
    }
}