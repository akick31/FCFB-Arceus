package com.fcfb.arceus.filters

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

class CorsFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD")
        response.addHeader(
            "Access-Control-Allow-Headers",
            "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"
        )
        response.addHeader(
            "Access-Control-Expose-Headers",
            "Access-Control-Allow-Origin, Access-Control-Allow-Credentials"
        )
        response.addHeader("Access-Control-Allow-Credentials", "false")
        response.addIntHeader("Access-Control-Max-Age", 10)
        filterChain.doFilter(request, response)
    }
}