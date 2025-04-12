package com.medscan.ai.domain.repositories

import com.medscan.ai.domain.entities.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserEntityRepository: MongoRepository<User, ObjectId>{
    fun findByEmail(email: String) : User?
}