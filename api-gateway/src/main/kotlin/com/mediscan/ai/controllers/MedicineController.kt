package com.mediscan.ai.controllers

import com.hopcape.common.api.MULTIPART_FORM_DATA
import com.hopcape.common.api.MedicineResource
import com.mediscan.ai.domain.medicine.UploadMedicineRequest
import com.mediscan.ai.domain.medicine.UploadMedicineRequestHandler
import com.mediscan.ai.domain.medicine.UploadMedicineResponse
import com.mediscan.ai.utils.VersionedRestControllerWithRequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

@VersionedRestControllerWithRequestMapping(
    version = 1,
    resource = MedicineResource.ROOT
)
class MedicineController(
    private val uploadMedicineRequestHandler: UploadMedicineRequestHandler
) {

    data class MedicineRequest(val name: String)

    @PostMapping(MedicineResource.IDENTIFY_ENDPOINT)
    fun identify(
        @RequestBody request: MedicineRequest
    ): String{
        return "Medicine name: ${request.name}"
    }

    @PostMapping(MedicineResource.UPLOAD_MEDICINE_IMAGE,consumes = [MULTIPART_FORM_DATA])
    fun uploadMedicineImage(
        @RequestPart("file") file: MultipartFile,
        @RequestPart("description", required = false) description: String?
    ): UploadMedicineResponse{
        return uploadMedicineRequestHandler.handleRequest(
            UploadMedicineRequest(
                image = file
            )
        )
    }
}