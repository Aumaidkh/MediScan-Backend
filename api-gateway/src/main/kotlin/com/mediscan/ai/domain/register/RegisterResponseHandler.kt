package com.mediscan.ai.domain.register

import com.hopcape.auth.application.AuthService
import com.mediscan.ai.domain.RequestHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

typealias RegisterResponseHandler = RequestHandler<RegisterRequest, RegisterResponse>

@Service
@Qualifier("registerRequestHandler")
class RegisterResponseHandlerImpl(
    private val service: AuthService
) : RegisterResponseHandler {

    override fun handleRequest(body: RegisterRequest): RegisterResponse {
        val response = service.register(
            email = body.email,
            password = body.password
        )

        return RegisterResponse(
            message = "User registered successfully."
        )
    }
}