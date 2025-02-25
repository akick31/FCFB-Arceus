package com.fcfb.arceus.tasks

import com.fcfb.arceus.service.auth.SessionService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class CleanupExpiredTokens(
    private val sessionService: SessionService,
) {
    @Scheduled(fixedRate = 3600000) // Every hour
    fun cleanUpExpiredTokens() {
        sessionService.clearExpiredTokens()
    }
}
