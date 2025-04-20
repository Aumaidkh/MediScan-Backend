package com.hopcape.cache.api

import com.hopcape.cache.model.CachedData
import com.hopcape.cache.repository.CachedDataRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull
/**
 * A service implementation of the [Cache] interface that uses MongoDB as the underlying storage mechanism.
 * This class interacts with the [CachedDataRepository] to store and retrieve cached data in JSON format.
 *
 * ### Key Features:
 * - Implements the [Cache] interface to provide standardized caching operations (get and update).
 * - Uses MongoDB for persistent storage of cached data, ensuring durability and scalability.
 * - Leverages [CachedDataRepository] for database interactions.
 *
 * ### Example Usage:
 * ```kotlin
 * @Service
 * class CacheService(
 *     private val cache: Cache
 * ) {
 *     fun getCachedData(key: String): String? {
 *         return cache.get(key)
 *     }
 *
 *     fun updateCachedData(key: String, data: String) {
 *         cache.update(key, data)
 *     }
 * }
 *
 * // Sample Input
 * val key = "medicineDetails"
 * val data = "Cipla Paracetamol Tablets IP PARACIP-500"
 *
 * // Update cache
 * cacheService.updateCachedData(key, data)
 *
 * // Retrieve cache
 * val cachedData = cacheService.getCachedData(key)
 *
 * // Sample Output
 * println(cachedData)
 * // Output:
 * // "Cipla Paracetamol Tablets IP PARACIP-500"
 * ```
 *
 * @property repository An instance of [CachedDataRepository] used to interact with the MongoDB collection.
 */
@Service
internal class MongoCache(
    private val repository: CachedDataRepository
) : Cache {

    /**
     * Retrieves the cached data associated with the specified key from MongoDB.
     *
     * @param key The key used to identify the cached data.
     * @return The cached data as a string, or `null` if the key does not exist.
     */
    override fun get(key: String): String? {
        return repository.findById(key).getOrNull()?.dataJson
    }

    /**
     * Updates or inserts the cached data for the specified key into MongoDB.
     *
     * @param key The key used to identify the cached data.
     * @param data The data to be stored in the cache.
     */
    override fun update(key: String, data: String) {
        repository.save(
            CachedData(
                id = key,
                dataJson = data
            )
        )
    }
}