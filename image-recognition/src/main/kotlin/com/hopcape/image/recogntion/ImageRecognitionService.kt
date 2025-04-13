package com.hopcape.image.recogntion

interface ImageRecognitionService {
    fun recognizeImage(imageBytes: ByteArray): String
}