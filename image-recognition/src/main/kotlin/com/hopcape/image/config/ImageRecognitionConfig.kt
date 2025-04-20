package com.hopcape.image.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.vision.v1.ImageAnnotatorClient
import com.google.cloud.vision.v1.ImageAnnotatorSettings
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.rekognition.RekognitionClient
import java.io.FileInputStream
/**
 * A configuration class that sets up clients for image recognition services provided by AWS Rekognition and Google Cloud Vision.
 * This class defines beans for [RekognitionClient] and [ImageAnnotatorClient], enabling dependency injection in a Spring application.
 *
 * ### Key Features:
 * - Configures AWS Rekognition client using AWS credentials (access key and secret key).
 * - Configures Google Cloud Vision client using a service account key file.
 * - Provides reusable beans for interacting with AWS Rekognition and Google Cloud Vision APIs.
 *
 * ### Configuration Requirements:
 * - **AWS Credentials**:
 *   - `aws.access.key.id`: The AWS access key ID for authenticating with AWS Rekognition.
 *   - `aws.secret.key`: The AWS secret key for authenticating with AWS Rekognition.
 * - **Google Cloud Vision Credentials**:
 *   - `google.cloud.vision.credentials.location`: The file path to the Google Cloud Vision service account key JSON file.
 *
 * ### Example Usage:
 * ```kotlin
 * // Example: Using the configured clients in a Spring application
 * @Autowired
 * private lateinit var rekognitionClient: RekognitionClient
 *
 * @Autowired
 * private lateinit var imageAnnotatorClient: ImageAnnotatorClient
 *
 * fun processImageWithAws(imageBytes: ByteArray) {
 *     val request = DetectTextRequest.builder()
 *         .image(Image.builder().bytes(SdkBytes.fromByteArray(imageBytes)).build())
 *         .build()
 *     val response = rekognitionClient.detectText(request)
 *     println(response.textDetections())
 * }
 *
 * fun processImageWithGoogleCloud(imageBytes: ByteArray) {
 *     val image = Image.newBuilder().setContent(ByteString.copyFrom(imageBytes)).build()
 *     val request = AnnotateImageRequest.newBuilder()
 *         .addFeatures(Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION))
 *         .setImage(image)
 *         .build()
 *     val response = imageAnnotatorClient.batchAnnotateImages(listOf(request))
 *     println(response.responsesList)
 * }
 * ```
 *
 * ### Beans:
 * - `rekognitionClient`: Configures and provides an instance of [RekognitionClient] for interacting with AWS Rekognition.
 * - `imageAnnotatorClient`: Configures and provides an instance of [ImageAnnotatorClient] for interacting with Google Cloud Vision.
 *
 * ### Notes:
 * - Ensure that the AWS credentials (`aws.access.key.id` and `aws.secret.key`) are valid and have permissions for Rekognition.
 * - Ensure that the Google Cloud Vision service account key file exists at the specified location and has the necessary permissions.
 */
@Configuration
internal class ImageRecognitionConfig {

    /**
     * The AWS access key ID used for authenticating with AWS Rekognition.
     * This value is injected from the application configuration property `aws.access.key.id`.
     */
    @Value("\${aws.access.key.id}")
    lateinit var accessKey: String

    /**
     * The AWS secret key used for authenticating with AWS Rekognition.
     * This value is injected from the application configuration property `aws.secret.key`.
     */
    @Value("\${aws.secret.key}")
    lateinit var secretKey: String

    /**
     * The file path to the Google Cloud Vision service account key JSON file.
     * This value is injected from the application configuration property `google.cloud.vision.credentials.location`.
     */
    @Value("\${google.cloud.vision.credentials.location}")
    lateinit var cloudVisionKeyPath: String

    /**
     * Configures and provides a bean for the AWS Rekognition client.
     *
     * This method creates an instance of [RekognitionClient] using the provided AWS credentials and region.
     *
     * @return An instance of [RekognitionClient] configured with the specified AWS credentials and region.
     */
    @Bean
    fun rekognitionClient(): RekognitionClient {
        val credentials = AwsBasicCredentials.create(accessKey, secretKey)
        return RekognitionClient.builder()
            .region(Region.US_EAST_1) // Replace with your desired region
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build()
    }

    /**
     * Configures and provides a bean for the Google Cloud Vision client.
     *
     * This method creates an instance of [ImageAnnotatorClient] using the service account key file specified by
     * `google.cloud.vision.credentials.location`.
     *
     * @return An instance of [ImageAnnotatorClient] configured with the specified Google Cloud credentials.
     */
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