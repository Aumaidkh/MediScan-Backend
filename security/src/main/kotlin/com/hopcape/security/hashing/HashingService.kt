package com.hopcape.security.hashing

interface HashingService {

    fun hash(raw: String): String

    fun verify(raw: String, hashed: String): Boolean
}