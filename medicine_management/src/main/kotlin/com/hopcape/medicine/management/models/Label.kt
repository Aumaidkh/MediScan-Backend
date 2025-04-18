package com.hopcape.medicine.management.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("labels")
data class Label(
    @Id val id: ObjectId = ObjectId(),
    @Indexed(unique = true)
    val text: String
)
