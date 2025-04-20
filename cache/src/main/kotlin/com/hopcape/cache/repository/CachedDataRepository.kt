package com.hopcape.cache.repository

import com.hopcape.cache.model.CachedData
import org.springframework.data.mongodb.repository.MongoRepository
/**
 * A repository interface for managing [CachedData] entities in MongoDB. This interface extends [MongoRepository]
 * to provide CRUD operations and other MongoDB-specific functionalities for the `cache` collection.
 *
 * ### Key Features:
 * - Provides methods for saving, retrieving, and deleting [CachedData] entities.
 * - Automatically generates query methods based on method names (e.g., `findById`).
 * - Integrates with Spring Data MongoDB for seamless database interactions.
 *
 * ### Example Usage:
 * ```kotlin
 * @Service
 * class CacheService(
 *     private val repository: CachedDataRepository
 * ) {
 *     fun getCachedData(key: String): String? {
 *         return repository.findById(key).orElse(null)?.dataJson
 *     }
 *
 *     fun updateCachedData(key: String, data: String) {
 *         repository.save(CachedData(id = key, dataJson = data))
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
internal interface CachedDataRepository : MongoRepository<CachedData, String>