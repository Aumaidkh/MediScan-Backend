package com.mediscan.ai.domain.refresh

import com.mediscan.ai.domain.RequestHandler
import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
    @field:NotBlank(
        message = "Refresh token cannot be null"
    )
    val refreshToken: String
): RequestHandler.Request
