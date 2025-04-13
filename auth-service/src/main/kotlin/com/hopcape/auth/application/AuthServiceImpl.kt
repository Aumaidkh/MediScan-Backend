package com.hopcape.auth.application

import com.hopcape.auth.domain.entities.User
import com.hopcape.auth.domain.repositories.UserEntityRepository
import com.hopcape.security.hashing.HashingService
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserEntityRepository,
    private val hashingService: HashingService
): AuthService {


    override fun register(email: String, password: String): User {
        return userRepository.save(
            User(
                email = email,
                password = hashingService.encode(password)
            )
        )
    }

    override fun findUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }
}