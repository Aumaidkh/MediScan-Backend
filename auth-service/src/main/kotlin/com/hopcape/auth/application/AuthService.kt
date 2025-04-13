package com.hopcape.auth.application

import com.hopcape.auth.domain.entities.User

interface AuthService {

    fun saveUser(email: String, password: String)

    fun findUserByEmail(email: String): User?
}