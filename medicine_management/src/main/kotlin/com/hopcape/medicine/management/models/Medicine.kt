package com.hopcape.medicine.management.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("medicines")
data class Medicine(
    @Id val id: ObjectId,
    val name: String,
)
