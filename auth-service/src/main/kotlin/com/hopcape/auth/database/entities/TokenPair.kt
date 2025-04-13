package com.hopcape.auth.database.entities

data class TokenPair(
    val accessToken: String,
    val refreshToken: String
)
