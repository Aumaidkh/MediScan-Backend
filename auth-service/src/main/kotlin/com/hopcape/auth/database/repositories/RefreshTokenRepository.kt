package com.hopcape.auth.database.repositories

import com.hopcape.auth.database.entities.RefreshToken
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository: MongoRepository<RefreshToken, ObjectId>{
    fun findByUserIdAndHashedToken(userId: ObjectId,hashedToken: String): RefreshToken?
}