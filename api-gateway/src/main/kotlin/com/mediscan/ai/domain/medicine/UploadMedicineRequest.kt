package com.mediscan.ai.domain.medicine

import com.mediscan.ai.domain.RequestHandler
import org.springframework.web.multipart.MultipartFile

data class UploadMedicineRequest(
    val image: MultipartFile
): RequestHandler.Request
