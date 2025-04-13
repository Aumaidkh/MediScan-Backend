package com.hopcape.security.hashing

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class BCryptHashingService : HashingService {

    private val bcrypt: BCryptPasswordEncoder = BCryptPasswordEncoder()

    override fun encode(raw: String): String {
        return bcrypt.encode(raw)
    }

    override fun matches(raw: String, hashed: String): Boolean {
        return bcrypt.matches(raw, hashed)
    }
}