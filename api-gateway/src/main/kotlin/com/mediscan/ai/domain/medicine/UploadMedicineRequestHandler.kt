package com.mediscan.ai.domain.medicine

import com.mediscan.ai.domain.RequestHandler
import org.springframework.stereotype.Service

typealias UploadMedicineRequestHandler = RequestHandler<UploadMedicineRequest, UploadMedicineResponse>

@Service
class UploadMedicineRequestHandlerImpl: UploadMedicineRequestHandler {

    override fun handleRequest(body: UploadMedicineRequest): UploadMedicineResponse {
        val image = body.image
        return UploadMedicineResponse(
            message = "Medicine uploaded successfully. ${image.size}"
        )
    }
}