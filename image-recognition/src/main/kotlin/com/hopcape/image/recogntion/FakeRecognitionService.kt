package com.hopcape.image.recogntion

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

const val FAKE_RECOGNITION_SERVICE = "FakeRecognitionService"

@Service
@Qualifier(FAKE_RECOGNITION_SERVICE)
internal class FakeRecognitionService(
    private val output: String = "Cipla Paracetamol Tablets IP PARACIP-500 रासप 10,Cipla,Paracetamol,Tablets,PARACIP,500,रासप"
): ImageRecognitionService {

    override fun recognizeImage(imageBytes: ByteArray): String {
        return output
    }
}