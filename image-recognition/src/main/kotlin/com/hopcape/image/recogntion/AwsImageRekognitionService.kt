package com.hopcape.image.recogntion

import org.springframework.stereotype.Service
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.rekognition.RekognitionClient
import software.amazon.awssdk.services.rekognition.model.DetectTextRequest
import software.amazon.awssdk.services.rekognition.model.Image

@Service
class AwsImageRekognitionService(
    private val client: RekognitionClient
): ImageRecognitionService {
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