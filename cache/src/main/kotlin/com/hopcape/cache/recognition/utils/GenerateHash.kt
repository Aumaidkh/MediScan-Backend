package com.hopcape.cache.recognition.utils

import java.security.MessageDigest
import java.util.*

typealias Hash = String
fun generateHashForImage(imageBytes: ByteArray): Hash {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(imageBytes)
    return Base64.getEncoder().encodeToString(hash)
}