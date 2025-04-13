package com.hopcape.auth.application

import com.hopcape.auth.domain.entities.TokenPair
import com.hopcape.auth.domain.entities.User

interface AuthService {

    fun register(email: String, password: String): User

    fun findUserByEmail(email: String): User?

    fun login(email: String, password: String): TokenPair
}