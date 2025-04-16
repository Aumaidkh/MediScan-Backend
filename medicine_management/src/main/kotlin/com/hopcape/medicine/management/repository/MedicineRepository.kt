package com.hopcape.medicine.management.repository

import com.hopcape.medicine.management.models.Medicine
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface MedicineRepository: MongoRepository<Medicine,ObjectId>