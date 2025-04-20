package com.hopcape.cache.predictions.repository

import com.hopcape.cache.predictions.model.CachedPrediction
import org.springframework.data.mongodb.repository.MongoRepository

interface CachedPredictionRepository: MongoRepository<CachedPrediction,String>