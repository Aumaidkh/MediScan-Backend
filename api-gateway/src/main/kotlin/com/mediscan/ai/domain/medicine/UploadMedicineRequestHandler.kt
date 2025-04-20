package com.mediscan.ai.domain.medicine

import com.hopcape.clustering.nlp.MedicineDetailsPredictor
import com.hopcape.image.recogntion.GOOGLE_CLOUD_VISION
import com.hopcape.image.recogntion.ImageRecognitionService
import com.mediscan.ai.domain.RequestHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

typealias UploadMedicineRequestHandler = RequestHandler<UploadMedicineRequest, UploadMedicineResponse>

@Service
internal class UploadMedicineRequestHandlerImpl(
    @Qualifier(GOOGLE_CLOUD_VISION)
    private val recognitionService: ImageRecognitionService,
    @Qualifier("GeminiMedicineDetailsPredictor")
    private val predictor: MedicineDetailsPredictor
): UploadMedicineRequestHandler {

    override fun handleRequest(body: UploadMedicineRequest): UploadMedicineResponse {
        val image = body.image
        val result = recognitionService.recognizeImage(
            imageBytes = image.bytes
        )
        val results = predictor.predictByLabels(result)
        with(results){
            return UploadMedicineResponse(
                message = this.name ?: "Unknown Medicine",
                name = this.name ?: "Unknown Medicine",
                details = this.description ?: "Unknown Medicine",
                usage = this.usage ?: "Unknown Medicine",
                dosage = this.dosages ?: "Unknown Medicine"
            )
        }
    }
}