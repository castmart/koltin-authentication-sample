package com.castmart.authentication.config.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter(val userDetailServiceSample: SampleAdminUserService, val jwtUtil: JWTUtil) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            val jwt = authorizationHeader.substring(7)
            val username = jwtUtil.extractUsername(jwt)

            if(username != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailServiceSample.loadUserByUsername(username)
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    val usernameAndPasswordAuthentictionToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    usernameAndPasswordAuthentictionToken.details = WebAuthenticationDetailsSource()
                        .buildDetails(request)
                    SecurityContextHolder.getContext().authentication = usernameAndPasswordAuthentictionToken
                }
            }
        }

        filterChain.doFilter(request, response)

    }
}