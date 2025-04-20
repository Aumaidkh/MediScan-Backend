package com.hopcape.cache.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

const val ONE_DAY_SECONDS = 60 * 60 * 24
/**
 * A data class representing cached data stored in MongoDB. This class maps to the `cache` collection and includes
 * fields for the cache key, JSON data, and creation timestamp with an expiration policy.
 *
 * ### Key Features:
 * - Maps to the `cache` collection in MongoDB using the `@Document` annotation.
 * - Includes an `id` field as the primary key and a `dataJson` field for storing cached data.
 * - Automatically expires documents after one day using the `@Indexed(expireAfterSeconds)` annotation.
 *
 * ### Example Usage:
 * ```kotlin
 * val cachedData = CachedData(
 *     id = "medicineDetails",
 *     dataJson = "Cipla Paracetamol Tablets IP PARACIP-500"
 * )
 *
 * println(cachedData)
 * // Output:
 * // CachedData(id=medicineDetails, dataJson=Cipla Paracetamol Tablets IP PARACIP-500, createdAt=2023-10-01T12:00:00Z)
 * ```
 *
 * @property id The unique identifier (primary key) for the cached data.
 * @property dataJson The JSON string representing the cached data.
 * @property createdAt The timestamp when the data was created. Documents expire after one day.
 */
@Document("cache")
internal data class CachedData(
    @Id val id: String,
    val dataJson: String,
    @Indexed(expireAfterSeconds = ONE_DAY_SECONDS)
    val createdAt: Instant = Instant.now()
)