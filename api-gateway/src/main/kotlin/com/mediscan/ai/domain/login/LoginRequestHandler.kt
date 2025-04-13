package com.mediscan.ai.domain.login

import com.hopcape.auth.application.AuthService
import com.mediscan.ai.domain.RequestHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

typealias LoginRequestHandler = RequestHandler<LoginRequest, LoginResponse>

@Service
@Qualifier("loginRequestHandler")
class LoginRequestHandlerImpl(
    private val service: AuthService
): LoginRequestHandler {


    override fun handleRequest(body: LoginRequest): LoginResponse {
        val authResponse =  service.login(
            email = body.email,
            password = body.password
        )

        return LoginResponse(
            accessToken = authResponse.accessToken,
            refreshToken = authResponse.refreshToken,
        )
    }

}