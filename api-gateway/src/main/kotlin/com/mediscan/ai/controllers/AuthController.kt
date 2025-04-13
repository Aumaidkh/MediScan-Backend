package com.mediscan.ai.controllers

import com.hopcape.auth.application.AuthService
import com.hopcape.common.api.AuthResource
import com.mediscan.ai.domain.login.LoginRequest
import com.mediscan.ai.domain.login.LoginRequestHandler
import com.mediscan.ai.domain.login.LoginResponse
import com.mediscan.ai.domain.refresh.RefreshTokenRequest
import com.mediscan.ai.domain.refresh.RefreshTokenRequestHandler
import com.mediscan.ai.domain.refresh.RefreshTokenResponse
import com.mediscan.ai.domain.register.RegisterRequest
import com.mediscan.ai.domain.register.RegisterResponse
import com.mediscan.ai.domain.register.RegisterResponseHandler
import com.mediscan.ai.utils.VersionedRestControllerWithRequestMapping
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@VersionedRestControllerWithRequestMapping(
    version = 1,
    resource = AuthResource.ROOT,
)
@Validated
class AuthController(
    @Qualifier("loginRequestHandler")
    private val loginRequestHandler: LoginRequestHandler,
    @Qualifier("registerRequestHandler")
    private val registerRequestHandler: RegisterResponseHandler,
    @Qualifier("refreshTokenRequestHandler")
    private val refreshTokenHandler: RefreshTokenRequestHandler
) {

    @PostMapping(AuthResource.LOGIN_ENDPOINT)
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): LoginResponse {
        return loginRequestHandler.handleRequest(
            LoginRequest(
                email = request.email,
                password = request.password
            )
        )
    }


    @PostMapping(AuthResource.REGISTER_ENDPOINT)
    fun register(
        @Valid @RequestBody request: RegisterRequest
    ): RegisterResponse {
        return registerRequestHandler.handleRequest(request)
    }


    @PostMapping(AuthResource.REFRESH_TOKEN_ENDPOINT)
    fun refresh(
        @Valid @RequestBody request: RefreshTokenRequest
    ): RefreshTokenResponse {
        return refreshTokenHandler.handleRequest(request)
    }
}