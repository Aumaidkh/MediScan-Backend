package com.hopcape.image.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.cloud.vision.v1.ImageAnnotatorSettings
import com.hopcape.image.recogntion.AwsImageRekognitionService
import com.hopcape.image.recogntion.ImageRecognitionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.rekognition.RekognitionClient
import java.io.FileInputStream

@Configuration
class ImageRecognitionConfig {
    @Value("\${aws.access.key.id}")
    lateinit var accessKey: String

    @Value("\${aws.secret.key}")
    lateinit var secretKey: String

    @Value("\${google.cloud.vision.credentials.location}")
    lateinit var cloudVisionKeyPath: String

    @Bean
    fun rekognitionClient(): RekognitionClient {
        val credentials = AwsBasicCredentials.create(accessKey, secretKey)
        return RekognitionClient.builder()
            .region(Region.US_EAST_1) // Replace with your desired region
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }

    @Bean
    fun imageAnnotatorClient(): ImageAnnotatorClient {
        val credentials = GoogleCredentials.fromStream(FileInputStream(cloudVisionKeyPath))
        return ImageAnnotatorClient.create(
            ImageAnnotatorSettings.newBuilder()
                .setCredentialsProvider { credentials }
                .build()
        )
    }

}