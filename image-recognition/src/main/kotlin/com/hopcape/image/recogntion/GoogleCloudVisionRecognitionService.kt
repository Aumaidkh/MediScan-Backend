package com.hopcape.image.recogntion

import com.google.cloud.vision.v1.AnnotateImageRequest
import com.google.cloud.vision.v1.Feature
import com.google.cloud.vision.v1.Image
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.protobuf.ByteString
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

const val GOOGLE_CLOUD_VISION = "GoogleCloudVisionRecognitionService"

@Service
@Qualifier(GOOGLE_CLOUD_VISION)
internal class GoogleCloudVisionRecognitionService(
    private val vision: ImageAnnotatorClient,
): ImageRecognitionService {
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
            .filter { it.description.length > 2 }
            .map { it.description.replace("\n"," ") }
            .filter { it.isNotBlank() }
            .distinct()
            .toList()


        return detectedTexts.joinToString(separator = ",")
    }
}