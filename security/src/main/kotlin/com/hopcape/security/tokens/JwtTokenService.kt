package com.hopcape.security.tokens

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class JwtTokenService(
    @Value("\${jwt.secret}") private val secret: String
): TokenService {

    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret))

    override fun generateToken(type: TokenService.TokenType, userId: String): String {
        return generateToken(
            userId = userId,
            type = type.type,
            validityInMillis = type.validity
        )
    }

    override fun validateToken(type: TokenService.TokenType, token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        return when(type){
            TokenService.TokenType.ACCESS -> validateAccessToken(claims)
            TokenService.TokenType.REFRESH -> validateRefreshToken(claims)
        }
    }

    override fun getUserIdFromToken(token: String): String {
        val claims = parseAllClaims(token) ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(401),
            "Invalid token"
        )
        return claims.subject ?: throw ResponseStatusException(
            HttpStatusCode.valueOf(401),
        )
    }

    private fun validateAccessToken(claims: Claims): Boolean {
        val type = claims["type"] as? String ?: return false
        return type == TokenService.TokenType.ACCESS.type
    }

    private fun validateRefreshToken(claims: Claims): Boolean {
        val type = claims["type"] as? String ?: return false
        return type == TokenService.TokenType.REFRESH.type
    }

    private fun generateToken(
        userId: String,
        type: String,
        validityInMillis: Long
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + validityInMillis)
        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    private fun parseAllClaims(token: String): Claims? {
        val rawToken = token.replace("Bearer ", "")
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(rawToken)
                .payload
        } catch (e: Exception){
            null
        }
    }
}