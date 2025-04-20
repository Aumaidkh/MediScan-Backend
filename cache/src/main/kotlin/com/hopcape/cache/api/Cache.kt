package com.hopcape.cache.api
/**
 * An interface defining the contract for a caching mechanism. This interface provides methods for retrieving and
 * updating cached data based on a key-value pair system.
 *
 * ### Key Features:
 * - Standardizes caching operations across different implementations (e.g., in-memory, Redis, MongoDB).
 * - Supports dependency injection in Spring applications for flexible caching strategies.
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
 */
interface Cache {

    /**
     * Retrieves the value associated with the specified key from the cache.
     *
     * @param key The key used to identify the cached data.
     * @return The value associated with the key, or `null` if the key does not exist in the cache.
     */
    fun get(key: String): String?

    /**
     * Updates the cache with a new value for the specified key.
     *
     * @param key The key used to identify the cached data.
     * @param data The value to be stored in the cache for the specified key.
     */
    fun update(key: String, data: String)
}