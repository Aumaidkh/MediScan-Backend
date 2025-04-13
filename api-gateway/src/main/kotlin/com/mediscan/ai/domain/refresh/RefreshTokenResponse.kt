package com.mediscan.ai.domain.refresh

import com.mediscan.ai.domain.RequestHandler

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
):RequestHandler.Response