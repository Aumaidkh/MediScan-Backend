package com.mediscan.ai.controllers

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
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody as SwaggerRequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(
    name = "Authentication",
    description = "Endpoints for user authentication including login, registration, and refreshing access tokens."
)
class AuthController(
    @Qualifier("loginRequestHandler")
    private val loginRequestHandler: LoginRequestHandler,
    @Qualifier("registerRequestHandler")
    private val registerRequestHandler: RegisterResponseHandler,
    @Qualifier("refreshTokenRequestHandler")
    private val refreshTokenHandler: RefreshTokenRequestHandler
) {

    @Operation(
        summary = "User Login",
        description = "Authenticates a user with a valid email and password. Returns an access and refresh token on success."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Authentication successful. Returns tokens.",
                content = [Content(schema = Schema(implementation = LoginResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Validation failed. Request body is malformed or missing required fields."
            ),
            ApiResponse(
                responseCode = "401",
                description = "Invalid credentials. Email or password is incorrect."
            )
        ]
    )
    @PostMapping(AuthResource.LOGIN_ENDPOINT)
    fun login(
        @Valid
        @RequestBody
        @SwaggerRequestBody(
            description = "Login request payload containing email and password.",
            required = true,
            content = [Content(schema = Schema(implementation = LoginRequest::class))]
        )
        request: LoginRequest
    ): LoginResponse {
        return loginRequestHandler.handleRequest(request)
    }

    @Operation(
        summary = "User Registration",
        description = "Registers a new user with a valid email and a strong password. Returns a confirmation message."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Registration successful. User has been created.",
                content = [Content(schema = Schema(implementation = RegisterResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input. Email format is incorrect or password does not meet requirements."
            ),
            ApiResponse(
                responseCode = "409",
                description = "Conflict. The email is already in use."
            )
        ]
    )
    @PostMapping(AuthResource.REGISTER_ENDPOINT)
    fun register(
        @Valid
        @RequestBody
        @SwaggerRequestBody(
            description = "Register request payload including email and password.",
            required = true,
            content = [Content(schema = Schema(implementation = RegisterRequest::class))]
        )
        request: RegisterRequest
    ): RegisterResponse {
        return registerRequestHandler.handleRequest(request)
    }

    @Operation(
        summary = "Refresh Access Token",
        description = "Generates a new access token using a valid refresh token. Keeps the user logged in without re-authenticating."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Token refreshed successfully. Returns a new token pair.",
                content = [Content(schema = Schema(implementation = RefreshTokenResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Validation error. Refresh token is missing or malformed."
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized. Refresh token is invalid or expired."
            )
        ]
    )
    @PostMapping(AuthResource.REFRESH_TOKEN_ENDPOINT)
    fun refresh(
        @Valid
        @RequestBody
        @SwaggerRequestBody(
            description = "Refresh token request payload containing a valid refresh token.",
            required = true,
            content = [Content(schema = Schema(implementation = RefreshTokenRequest::class))]
        )
        request: RefreshTokenRequest
    ): RefreshTokenResponse {
        return refreshTokenHandler.handleRequest(request)
    }
}