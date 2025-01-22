package com.fcfb.arceus.service.fcfb

import com.fcfb.arceus.domain.RequestMessageLog
import com.fcfb.arceus.repositories.RequestMessageLogRepository
import org.springframework.stereotype.Component
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Component
class RequestMessageLogService(
    private val requestMessageLogRepository: RequestMessageLogRepository,
) {
    /**
     * Log the request message
     * @param requestMessageLog
     * @return
     */
    fun logRequestMessage(requestMessageLog: RequestMessageLog): RequestMessageLog {
        requestMessageLog.messageTs =
            ZonedDateTime.now(
                ZoneId.of("America/New_York"),
            ).format(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss"))
        return requestMessageLogRepository.save(requestMessageLog)
    }
}
