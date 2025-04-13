package com.hopcape.security.hashing

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
@Qualifier("passwordHasher")
class BCryptHashingService : HashingService {

    private val bcrypt: BCryptPasswordEncoder = BCryptPasswordEncoder()

    override fun hash(raw: String): String {
        return bcrypt.encode(raw)
    }

    override fun verify(raw: String, hashed: String): Boolean {
        return bcrypt.matches(raw, hashed)
    }
}