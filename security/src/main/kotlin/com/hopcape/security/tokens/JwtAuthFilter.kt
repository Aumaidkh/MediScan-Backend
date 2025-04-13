package com.hopcape.security.tokens

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val tokenService: TokenService
): OncePerRequestFilter() {


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: jakarta.servlet.http.HttpServletResponse,
        filterChain: jakarta.servlet.FilterChain
    ) {
        request
            .getAuthorizationHeader()?.let { token ->
                if (tokenService.validateToken(
                    token = token,
                    type = TokenService.TokenType.ACCESS
                )){
                    val userId = tokenService.getUserIdFromToken(token)
                    val auth = UsernamePasswordAuthenticationToken(userId,null,null)
                    SecurityContextHolder.getContext().authentication = auth
                }
            }

        filterChain.doFilter(request,response)
    }

    private fun HttpServletRequest.getAuthorizationHeader(): String? {
       val header = getHeader("Authorization")
       return header?.substringAfter("Bearer ")
    }
}