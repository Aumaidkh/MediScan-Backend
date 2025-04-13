package com.hopcape.security.tokens

import kotlin.time.DurationUnit
import kotlin.time.toDuration

interface TokenService {

    fun validateToken(type: TokenType, token: String): Boolean

    fun getUserIdFromToken(token: String): String

    fun generateToken(type: TokenType, userId: String): String

    enum class TokenType(val type: String,val validity: Long){
        ACCESS(
            type = "access",
            validity = 15.toDuration(DurationUnit.MINUTES).inWholeMilliseconds
        ),
        REFRESH(
            type = "refresh",
            validity = 1.toDuration(DurationUnit.DAYS).inWholeMilliseconds
        )
    }

}