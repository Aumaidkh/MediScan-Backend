package com.hopcape.image.recogntion

interface ImageRecognitionService {
    fun recognizeImage(image: ByteArray): String
}