package com.hopcape.com.hopcape.medicine.management.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("training_data")
data class TrainingData(
    @Id val id: ObjectId,
    val text: String
)
