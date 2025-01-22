package com.fcfb.arceus.service.log

import com.fcfb.arceus.domain.CoachTransactionLog
import com.fcfb.arceus.repositories.CoachTransactionLogRepository
import org.springframework.stereotype.Component

@Component
class CoachTransactionLogService(
    private val coachTransactionLogRepository: CoachTransactionLogRepository,
) {
    /**
     * Log a coach transaction
     */
    fun logCoachTransaction(transaction: CoachTransactionLog) = coachTransactionLogRepository.save(transaction)

    /**
     * Get the entire coach transaction log
     */
    fun getEntireCoachTransactionLog() = coachTransactionLogRepository.getEntireCoachTransactionLog()
}
