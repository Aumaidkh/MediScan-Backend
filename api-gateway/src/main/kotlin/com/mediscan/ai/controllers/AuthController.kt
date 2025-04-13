package com.mediscan.ai.controllers

import com.hopcape.auth.application.AuthService
import com.hopcape.security.hashing.HashingService
import com.mediscan.ai.utils.VersionedRestControllerWithRequestMapping
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

private const val LOGIN_ENDPOINT = "/login"
private const val REGISTER_ENDPOINT = "/register"
private const val REFRESH_ENDPOINT = "/refresh"

@VersionedRestControllerWithRequestMapping(
    version = 1,
    resource = "/auth"
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

    @PostMapping(LOGIN_ENDPOINT)
    fun login(
        @RequestBody request: LoginRequest
    ): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(
            LoginResponse(
                accessToken = "accessToken",
                refreshToken = "refreshToken"
            )
        )
    }


    data class RegisterRequest(
        val email: String,
        val password: String
    )
    data class RegisterResponse(
        val message: String
    )
    @PostMapping(REGISTER_ENDPOINT)
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

    @PostMapping(REFRESH_ENDPOINT)
    fun refresh(
        @RequestBody request: RefreshRequest
    ): ResponseEntity<RefreshResponse> {
        return ResponseEntity.ok(
            RefreshResponse(
                newAccessToken = "newAccessToken",
                rewRefreshToken = "rewRefreshToken"
            )
        )
    }
}