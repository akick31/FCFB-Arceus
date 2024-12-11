package com.fcfb.arceus.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.boot.actuate.health.Status
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/health")
class HealthController {
    @Autowired
    private val healthEndpoint: HealthEndpoint? = null

    @GetMapping("")
    fun healthCheck(): ResponseEntity<String> {
        val health = healthEndpoint?.health()
        return if (health?.status == Status.UP) {
            ResponseEntity.ok("Application is healthy")
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Application is unhealthy")
        }
    }
}
