package com.hopcape.cache.api

import com.hopcape.cache.predictions.model.CachedPrediction
import com.hopcape.cache.predictions.repository.CachedPredictionRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
internal class CacheImpl(
    private val repository: CachedPredictionRepository
): Cache {
    override fun getCachedDetails(labels: String): String? {
        return repository.findById(labels).getOrNull()?.resultJson
    }

    override fun cacheDetails(labels: String, detailsJson: String) {
        repository.save(
            CachedPrediction(
                id = labels,
                labels = labels,
                resultJson = detailsJson
            )
        )
    }
}