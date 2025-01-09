package com.fcfb.arceus.service.fcfb

import com.fcfb.arceus.domain.CoachTransactionLog
import com.fcfb.arceus.domain.Game
import com.fcfb.arceus.domain.Season
import com.fcfb.arceus.repositories.CoachTransactionLogRepository
import com.fcfb.arceus.repositories.SeasonRepository
import com.fcfb.arceus.utils.CurrentSeasonNotFoundException
import com.fcfb.arceus.utils.CurrentWeekNotFoundException
import org.springframework.stereotype.Component
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
