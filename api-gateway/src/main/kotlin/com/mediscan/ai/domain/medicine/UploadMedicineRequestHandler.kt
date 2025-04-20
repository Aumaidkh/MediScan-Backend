package com.mediscan.ai.domain.medicine

import com.hopcape.clustering.api.MedicineDetailsPredictor
import com.hopcape.clustering.fake.FAKE_PREDICTOR
import com.hopcape.image.recogntion.fake.FAKE_RECOGNITION_SERVICE
import com.hopcape.image.recogntion.api.ImageRecognitionService
import com.mediscan.ai.domain.RequestHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

typealias UploadMedicineRequestHandler = RequestHandler<UploadMedicineRequest, UploadMedicineResponse>

@Service
internal class UploadMedicineRequestHandlerImpl(
    @Qualifier(FAKE_RECOGNITION_SERVICE)
    private val recognitionService: ImageRecognitionService,
    @Qualifier(FAKE_PREDICTOR)
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