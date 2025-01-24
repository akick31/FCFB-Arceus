package com.fcfb.arceus.config

import org.apache.catalina.connector.Connector
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class HttpRedirectConfig {
    @Bean
    open fun servletContainer(): ServletWebServerFactory {
        val connector = Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL)
        connector.scheme = "http"
        connector.port = 1313
        connector.secure = false
        connector.redirectPort = 1314

        val tomcatFactory = TomcatServletWebServerFactory()
        tomcatFactory.addAdditionalTomcatConnectors(connector)
        return tomcatFactory
    }
}