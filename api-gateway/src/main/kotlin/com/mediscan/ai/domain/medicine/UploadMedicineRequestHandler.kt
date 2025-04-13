package com.mediscan.ai.domain.medicine

import com.hopcape.image.recogntion.ImageRecognitionService
import com.mediscan.ai.domain.RequestHandler
import org.springframework.stereotype.Service

typealias UploadMedicineRequestHandler = RequestHandler<UploadMedicineRequest, UploadMedicineResponse>

@Service
class UploadMedicineRequestHandlerImpl(
    private val recognitionService: ImageRecognitionService
): UploadMedicineRequestHandler {

    override fun handleRequest(body: UploadMedicineRequest): UploadMedicineResponse {
        val image = body.image
        recognitionService.recognizeImage(
            imageBytes = image.bytes
        )
        return UploadMedicineResponse(
            message = "Medicine uploaded successfully. ${image.size}"
        )
    }
}