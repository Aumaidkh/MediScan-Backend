package com.hopcape.auth.domain.entities

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)
