package com.mediscan.ai.controllers

import com.hopcape.auth.application.AuthService
import com.hopcape.auth.database.entities.TokenPair
import com.hopcape.common.api.AuthResource
import com.mediscan.ai.domain.login.LoginRequest
import com.mediscan.ai.domain.login.LoginRequestHandler
import com.mediscan.ai.domain.login.LoginResponse
import com.mediscan.ai.utils.VersionedRestControllerWithRequestMapping
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
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
    private val authService: AuthService,
    @Qualifier("loginRequestHandler")
    private val loginRequestHandler: LoginRequestHandler
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


    data class RegisterRequest(
        @field:Email(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email"
        )
        val email: String,
        @field:Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must be at least 8 characters long, contain at least one digit, one lowercase letter, one uppercase letter, one special character (@#$%^&+=), and must not contain spaces."
        )
        val password: String
    )

    data class RegisterResponse(
        val message: String
    )
    @PostMapping(AuthResource.REGISTER_ENDPOINT)
    fun register(
        @Valid @RequestBody request: RegisterRequest
    ): RegisterResponse {
        return with(authService.register(request.email, request.password)){
            RegisterResponse("User registered successfully")
        }
    }


    data class RefreshRequest(
        @field:NotBlank
        val refreshToken: String
    )

    @PostMapping(AuthResource.REFRESH_TOKEN_ENDPOINT)
    fun refresh(
        @Valid @RequestBody request: RefreshRequest
    ): TokenPair {
        return authService.refreshToken(request.refreshToken)
    }
}