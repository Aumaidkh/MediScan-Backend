package com.hopcape.auth.application

import com.hopcape.auth.database.entities.TokenPair
import com.hopcape.auth.database.entities.User

interface AuthService {

    fun register(email: String, password: String): User

    fun findUserByEmail(email: String): User?

    fun login(email: String, password: String): TokenPair
}