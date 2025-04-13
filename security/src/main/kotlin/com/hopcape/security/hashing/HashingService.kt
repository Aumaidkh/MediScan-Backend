package com.hopcape.security.hashing

interface HashingService {

    fun encode(raw: String): String

    fun matches(raw: String, hashed: String): Boolean
}