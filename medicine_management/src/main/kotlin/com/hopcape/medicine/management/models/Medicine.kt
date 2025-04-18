package com.hopcape.medicine.management.models

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document("medicine")
data class Medicine(
    @Id val _id: ObjectId = ObjectId(),
    @Indexed(unique = true)
    val name: String,
    val id: String,
    val price: Double?,
    val Is_discontinued: Boolean?,
    val manufacturer_name: String?,
    val type: String?,
    val pack_size_label: String?,
    val short_composition_1: String?,
    val short_composition_2: String?
)
