package com.mediscan.ai.domain.login

import com.mediscan.ai.domain.RequestHandler

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
): RequestHandler.Response