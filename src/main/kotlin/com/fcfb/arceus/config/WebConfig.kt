package com.fcfb.arceus.config

import com.fcfb.arceus.filters.CorsFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
open class WebConfig : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .antMatchers("/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement()
            .invalidSessionUrl("/invalidSession") // Redirect to /invalidSession if the session is invalid
            .maximumSessions(1) // Allow only one session per user
            .maxSessionsPreventsLogin(false) // Allow multiple logins (if false, prevents new login when maximum sessions is reached)
            .expiredUrl("/sessionExpired") // Redirect to /sessionExpired if the session is expired
            .and()
    }
}

