package com.hopcape.auth.database.repositories

import com.hopcape.auth.database.entities.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserEntityRepository: MongoRepository<User, ObjectId>{
    fun findByEmail(email: String) : User?
}