package com.mediscan.ai.domain.register

import com.mediscan.ai.domain.RequestHandler

data class RegisterResponse(
    val message: String
): RequestHandler.Response
