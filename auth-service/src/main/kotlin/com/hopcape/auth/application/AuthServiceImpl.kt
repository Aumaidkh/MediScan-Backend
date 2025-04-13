package com.hopcape.auth.application

import com.hopcape.auth.database.entities.RefreshToken
import com.hopcape.auth.database.entities.TokenPair
import com.hopcape.auth.database.entities.User
import com.hopcape.auth.database.repositories.RefreshTokenRepository
import com.hopcape.auth.database.repositories.UserEntityRepository
import com.hopcape.security.hashing.HashingService
import com.hopcape.security.tokens.TokenService
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Service
class AuthServiceImpl(
    private val userRepository: UserEntityRepository,
    @Qualifier("passwordHasher")
    private val passwordHasher: HashingService,
    @Qualifier("tokenHasher")
    private val tokenHasher: HashingService,
    private val tokenService: TokenService,
    private val tokenRepository: RefreshTokenRepository
): AuthService {


    override fun register(email: String, password: String): User {
        return userRepository.save(
            User(
                email = email,
                password = passwordHasher.hash(password)
            )
        )
    }

    override fun findUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    override fun login(email: String, password: String): TokenPair {
        val user = userRepository.findByEmail(email) ?: throw BadCredentialsException("Invalid credentials")

        if (!passwordHasher.verify(password, user.password)) throw BadCredentialsException("Invalid credentials")

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
        ).also {
            it.storeRefreshToken(user.id)
        }
    }

    @Transactional
    override fun refreshToken(refreshToken: String): TokenPair {
        if (!tokenService.validateToken(
            type = TokenService.TokenType.REFRESH,
            token = refreshToken
        )){
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid refresh token")
        }

        val userId = tokenService.getUserIdFromToken(refreshToken)
        val user = userRepository.findById(ObjectId(userId)).orElseThrow {
            ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "User not found"
            )
        }

        val hashedToken = tokenHasher.hash(refreshToken)
        val existingRefreshToken = tokenRepository.findByUserIdAndHashedToken(user.id,hashedToken) ?: throw ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Invalid refresh token"
        )

        tokenRepository.delete(existingRefreshToken)

        return TokenPair(
            accessToken = tokenService.generateToken(type = TokenService.TokenType.ACCESS, userId = user.id.toHexString()),
            refreshToken = tokenService.generateToken(type = TokenService.TokenType.REFRESH, userId = user.id.toHexString())
        ).also {
            it.storeRefreshToken(user.id)
        }

    }

    private fun TokenPair.storeRefreshToken(userId: ObjectId){
        val hashedToken = tokenHasher.hash(refreshToken)
        val expiresAt = Instant.now().plusMillis(TokenService.TokenType.REFRESH.validity)
        tokenRepository.save(
            RefreshToken(
                userId = userId,
                expiresAt = expiresAt,
                hashedToken = hashedToken,
                createdAt = Instant.now()
            )
        )
    }
}