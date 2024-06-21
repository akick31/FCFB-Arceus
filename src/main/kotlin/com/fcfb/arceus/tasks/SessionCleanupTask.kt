package com.fcfb.arceus.tasks

import com.fcfb.arceus.repositories.SessionRepository
import com.fcfb.arceus.utils.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SessionCleanupTask {

    @Autowired
    private lateinit var sessionRepository: SessionRepository

    @Scheduled(fixedRate = 60000) // Cleanup every minute
    fun cleanupExpiredSessions() {
        val now = LocalDateTime.now()
        val expiredSessions = sessionRepository.findByExpirationTimeBefore(now)
        sessionRepository.deleteAll(expiredSessions)
        Logger.debug("${expiredSessions.size} sessions were expired and cleared")
    }
}
