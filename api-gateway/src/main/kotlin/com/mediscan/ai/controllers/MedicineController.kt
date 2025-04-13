package com.mediscan.ai.controllers

import com.hopcape.common.api.MedicineResource
import com.mediscan.ai.utils.VersionedRestControllerWithRequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@VersionedRestControllerWithRequestMapping(
    version = 1,
    resource = MedicineResource.ROOT
)
class MedicineController {

    data class MedicineRequest(val name: String)

    @PostMapping(MedicineResource.IDENTIFY_ENDPOINT)
    fun identify(
        @RequestBody request: MedicineRequest
    ): String{
        return "Medicine name: ${request.name}"
    }
}