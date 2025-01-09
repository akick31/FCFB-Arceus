package com.fcfb.arceus.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
open class WebConfig : WebSecurityConfigurerAdapter(), WebMvcConfigurer {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS).permitAll()  // Allow pre-flight requests
            .antMatchers("/**").permitAll()  // Allow all paths for now, modify as per needs
            .anyRequest().authenticated()
            .and()
            .sessionManagement()
            .invalidSessionUrl("/invalidSession")
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
            .expiredUrl("/sessionExpired")
            .and()
    }

    // Add CORS configuration
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://fakecollegefootball.com",
                "https://fakecollegefootball.com"
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true)
            .maxAge(3600)
    }
}