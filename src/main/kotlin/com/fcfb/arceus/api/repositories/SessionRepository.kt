package com.fcfb.arceus.api.repositories

import com.fcfb.arceus.models.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface SessionRepository : JpaRepository<Session, Long> {

    fun deleteByToken(token: String)

    fun findByExpirationTimeBefore(expirationTime: LocalDateTime): List<Session>
}
