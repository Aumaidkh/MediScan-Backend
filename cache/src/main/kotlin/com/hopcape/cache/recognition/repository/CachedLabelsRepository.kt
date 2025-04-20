package com.hopcape.cache.recognition.repository

import com.hopcape.cache.recognition.model.CachedLabels
import org.springframework.data.mongodb.repository.MongoRepository

interface CachedLabelsRepository: MongoRepository<CachedLabels,String>