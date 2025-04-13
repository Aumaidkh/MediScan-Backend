package com.mediscan.ai.domain.login

import com.mediscan.ai.domain.RequestHandler
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Pattern

data class LoginRequest(
    @field:Email(
        message = "Invalid email"
    )
    val email: String,
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
        message = "Password must be at least 8 characters long, contain at least one digit, one lowercase letter, one uppercase letter, one special character (@#$%^&+=), and must not contain spaces."
    )
    val password: String
): RequestHandler.Request