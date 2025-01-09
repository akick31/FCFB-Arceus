package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.CoachTransactionLog
import com.fcfb.arceus.domain.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CoachTransactionLogRepository : CrudRepository<CoachTransactionLog?, String?> {
    @Query("SELECT * FROM coach_transaction_log", nativeQuery = true)
    fun getEntireCoachTransactionLog(): List<CoachTransactionLog>
}
