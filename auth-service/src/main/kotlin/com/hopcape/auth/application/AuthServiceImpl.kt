package com.hopcape.auth.application

import com.hopcape.auth.domain.entities.User
import com.hopcape.auth.domain.repositories.UserEntityRepository
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserEntityRepository
): AuthService {


    override fun saveUser(email: String, password: String) {
        userRepository.save(User(email = email, password = password))
    }

    override fun findUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }
}