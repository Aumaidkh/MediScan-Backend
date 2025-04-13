package com.hopcape.image.recogntion

import org.springframework.stereotype.Service

@Service
class AwsImageRekognitionService: ImageRecognitionService {
    override fun recognizeImage(image: ByteArray): String {
        TODO("Not yet implemented")
    }
}