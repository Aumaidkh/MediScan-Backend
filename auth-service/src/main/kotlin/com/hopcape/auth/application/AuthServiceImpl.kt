package com.hopcape.auth.application

import com.hopcape.auth.domain.entities.TokenPair
import com.hopcape.auth.domain.entities.User
import com.hopcape.auth.domain.repositories.UserEntityRepository
import com.hopcape.security.hashing.HashingService
import com.hopcape.security.tokens.TokenService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service

@Service
class AuthServiceImpl(
    private val userRepository: UserEntityRepository,
    private val hashingService: HashingService,
    private val tokenService: TokenService
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

    override fun login(email: String, password: String): TokenPair {
        val user = userRepository.findByEmail(email) ?: throw BadCredentialsException("Invalid credentials")

        if (!hashingService.matches(password, user.password)) throw BadCredentialsException("Invalid credentials")

        val accessToken = tokenService.generateToken(
            type = TokenService.TokenType.ACCESS,
            userId = user.id.toHexString()
        )

        val refreshToken = tokenService.generateToken(
            type = TokenService.TokenType.REFRESH,
            userId = user.id.toHexString()
        )

        return TokenPair(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}