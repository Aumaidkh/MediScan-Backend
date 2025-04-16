package com.hopcape.com.hopcape.medicine.management.repository

import com.hopcape.com.hopcape.medicine.management.models.TrainingData
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface TrainingDataRepository: MongoRepository<TrainingData, ObjectId>