package com.mediscan.ai.domain.medicine

import com.mediscan.ai.domain.RequestHandler

data class UploadMedicineResponse(
    val message: String
): RequestHandler.Response
