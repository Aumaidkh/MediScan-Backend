package com.mediscan.ai.controllers

import com.hopcape.auth.application.AuthService
import com.hopcape.auth.database.entities.TokenPair
import com.hopcape.common.api.AuthResource
import com.mediscan.ai.utils.VersionedRestControllerWithRequestMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@VersionedRestControllerWithRequestMapping(
    version = 1,
    resource = AuthResource.ROOT,
)
class AuthController(
    private val authService: AuthService
) {

    data class LoginRequest(
        val email: String,
        val password: String
    )

    data class LoginResponse(
        val accessToken: String,
        val refreshToken: String
    )

    @PostMapping(AuthResource.LOGIN_ENDPOINT)
    fun login(
        @RequestBody request: LoginRequest
    ): TokenPair {
        return authService.login(request.email, request.password)
    }


    data class RegisterRequest(
        val email: String,
        val password: String
    )
    data class RegisterResponse(
        val message: String
    )
    @PostMapping(AuthResource.REGISTER_ENDPOINT)
    fun register(
        @RequestBody request: RegisterRequest
    ): RegisterResponse {
        return with(authService.register(request.email, request.password)){
            RegisterResponse("User registered successfully")
        }
    }


    data class RefreshRequest(
        val refreshToken: String
    )

    data class RefreshResponse(
        val newAccessToken: String,
        val rewRefreshToken: String
    )

    @PostMapping(AuthResource.REFRESH_TOKEN_ENDPOINT)
    fun refresh(
        @RequestBody request: RefreshRequest
    ): TokenPair {
        return authService.refreshToken(request.refreshToken)
    }
}