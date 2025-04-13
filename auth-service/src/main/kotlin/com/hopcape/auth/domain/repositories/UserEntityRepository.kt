package com.hopcape.auth.domain.repositories

import com.hopcape.auth.domain.entities.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserEntityRepository: MongoRepository<User, ObjectId>{
    fun findByEmail(email: String) : User?
}