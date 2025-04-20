package com.hopcape.cache.recognition.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

const val ONE_DAY_SECONDS = 60 * 60 * 24

@Document("cached_labels")
data class CachedLabels(
    @Id val id: String,
    val labels: String,
    @Indexed(expireAfterSeconds = ONE_DAY_SECONDS)
    val createdAt: Instant = Instant.now()
)
