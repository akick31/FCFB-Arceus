package com.fcfb.arceus.config

import com.fcfb.arceus.filters.CorsFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class WebConfig : WebSecurityConfigurerAdapter() {
    @Bean
    fun corsFilter(): CorsFilter {
        return CorsFilter()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/arceus/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .csrf().disable()
    }
}

