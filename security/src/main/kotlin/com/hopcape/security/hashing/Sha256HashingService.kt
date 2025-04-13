package com.hopcape.security.hashing

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.Base64

@Component
@Qualifier("tokenHasher")
class Sha256HashingService: HashingService {
    private val digest by lazy { MessageDigest.getInstance("SHA-256") }

    override fun encode(raw: String): String {
        val hashBytes = digest.digest(raw.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }

    override fun matches(raw: String, hashed: String): Boolean {
        return encode(raw) == hashed
    }
}