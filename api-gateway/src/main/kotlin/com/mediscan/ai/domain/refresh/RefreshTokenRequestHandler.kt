package com.mediscan.ai.domain.refresh

import com.hopcape.auth.application.AuthService
import com.mediscan.ai.domain.RequestHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

typealias RefreshTokenRequestHandler = RequestHandler<RefreshTokenRequest, RefreshTokenResponse>

@Service
@Qualifier("refreshTokenRequestHandler")
class RefreshTokenRequestHandlerImpl(
    private val service: AuthService
) : RefreshTokenRequestHandler {

    override fun handleRequest(body: RefreshTokenRequest): RefreshTokenResponse {
        val response = service.refreshToken(
            refreshToken = body.refreshToken
        )
        return RefreshTokenResponse(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
        )
    }
}