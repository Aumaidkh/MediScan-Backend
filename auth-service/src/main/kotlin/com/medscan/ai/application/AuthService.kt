package com.medscan.ai.application

import com.medscan.ai.domain.entities.User

interface AuthService {

    fun saveUser(email: String, password: String)

    fun findUserByEmail(email: String): User?
}