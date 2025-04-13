package com.hopcape.auth.database.entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("refresh_tokens")
data class RefreshToken(
    @Id val userId: ObjectId,
    val hashedToken: String,
    val expiresAt: Instant,
    val createdAt: Instant = Instant.now()
)