package com.fcfb.arceus.repositories

import com.fcfb.arceus.domain.CoachTransactionLog
import com.fcfb.arceus.domain.User.CoachPosition
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CoachTransactionLogRepository : CrudRepository<CoachTransactionLog, Int> {
    @Query("SELECT * FROM coach_transaction_log", nativeQuery = true)
    fun getEntireCoachTransactionLog(): List<CoachTransactionLog>

    fun findByTeam(team: String): List<CoachTransactionLog>

    fun findByTransaction(transaction: CoachTransactionLog.TransactionType): List<CoachTransactionLog>

    fun findByPosition(position: CoachPosition): List<CoachTransactionLog>

    fun findByProcessedBy(processedBy: String): List<CoachTransactionLog>
}
