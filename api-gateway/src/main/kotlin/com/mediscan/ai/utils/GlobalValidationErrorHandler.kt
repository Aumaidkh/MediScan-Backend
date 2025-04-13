package com.mediscan.ai.utils

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalValidationErrorHandler {


    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(exception: MethodArgumentNotValidException): ResponseEntity<Map<String,String>> {
        val errors = exception.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Unknown Error") }
        return ResponseEntity.badRequest().body(errors)
    }
}