package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.AuditLog
import com.fcfb.arceus.domain.CoachTransactionLog
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AuditLogRepository : CrudRepository<AuditLog?, String?> {
    @Query("SELECT * FROM audit_log", nativeQuery = true)
    fun getEntireAuditLog(): List<AuditLog>
}
