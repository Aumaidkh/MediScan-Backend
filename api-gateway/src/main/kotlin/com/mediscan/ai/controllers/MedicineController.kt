package com.mediscan.ai.controllers

import com.mediscan.ai.utils.VersionedRestControllerWithRequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@VersionedRestControllerWithRequestMapping(
    version = 1,
    resource = "/medicine"
)
class MedicineController {

    data class MedicineRequest(val name: String)

    @PostMapping("/identify")
    fun identify(
        @RequestBody request: MedicineRequest
    ): String{
        return "Medicine name: ${request.name}"
    }
}