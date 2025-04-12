package com.mediscan.ai.utils

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private const val VERSION_PATH = "/v{version}"
private const val RESOURCE_PATH = "/{resource}"
private const val API_PATH = "/api"


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@RestController
@RequestMapping(API_PATH + VERSION_PATH + RESOURCE_PATH)
annotation class VersionedRestControllerWithRequestMapping(
    val version: Int,
    val resource: String,
)