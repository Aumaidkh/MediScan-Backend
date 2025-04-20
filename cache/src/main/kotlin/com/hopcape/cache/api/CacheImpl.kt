package com.hopcape.cache.api

import com.hopcape.cache.predictions.model.CachedPrediction
import com.hopcape.cache.predictions.repository.CachedPredictionRepository
import com.hopcape.cache.recognition.model.CachedLabels
import com.hopcape.cache.recognition.repository.CachedLabelsRepository
import com.hopcape.cache.recognition.utils.generateHashForImage
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
internal class CacheImpl(
    private val detailsRepository: CachedPredictionRepository,
    private val labelsRepository: CachedLabelsRepository
): Cache {
    override fun getCachedDetails(labels: String): String? {
        return detailsRepository.findById(labels).getOrNull()?.resultJson
    }

    override fun cacheDetails(labels: String, detailsJson: String) {
        detailsRepository.save(
            CachedPrediction(
                id = labels,
                labels = labels,
                resultJson = detailsJson
            )
        )
    }

    override fun getCachedLabels(bytes: ByteArray): String? {
        val hash = generateHashForImage(bytes)
        return labelsRepository.findById(hash).getOrNull()?.labels
    }

    override fun cacheLabels(labels: String, bytes: ByteArray) {
        val hash = generateHashForImage(bytes)
        labelsRepository.save(
            CachedLabels(
                id = hash,
                labels = labels
            )
        )
    }
}