package com.hopcape.image.recogntion

/**
 * Interface for image recognition services.
 *
 * Provides a contract for implementing an image recognition service
 * that processes an image and returns recognized data as a string.
 */
interface ImageRecognitionService {
    /**
     * Recognizes and extracts text or other data from the given image.
     *
     * @param imageBytes The image data as a byte array to be processed for recognition.
     * @return A string representing the recognized content from the image.
     */
    fun recognizeImage(imageBytes: ByteArray): String
}