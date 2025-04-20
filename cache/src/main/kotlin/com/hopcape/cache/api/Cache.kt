package com.hopcape.cache.api

interface Cache {
    fun getCachedDetails(labels: String): String?
    fun cacheDetails(labels: String,detailsJson: String)

    fun getCachedLabels(bytes: ByteArray): String?
    fun cacheLabels(labels: String,bytes: ByteArray)
}