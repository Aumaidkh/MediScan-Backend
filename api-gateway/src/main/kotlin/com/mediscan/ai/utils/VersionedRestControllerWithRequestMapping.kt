package com.mediscan.ai.utils

import com.hopcape.common.api.API_PATH
import com.hopcape.common.api.VERSION_PREFIX
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private const val VERSION_PATH = "$VERSION_PREFIX{version}"
private const val RESOURCE_PATH = "/{resource}"


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@RestController
@RequestMapping(API_PATH + VERSION_PATH + RESOURCE_PATH)
annotation class VersionedRestControllerWithRequestMapping(
    val version: Int,
    val resource: String,
)