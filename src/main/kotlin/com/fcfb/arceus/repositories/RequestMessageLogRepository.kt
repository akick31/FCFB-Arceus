package com.fcfb.arceus.repositories

import com.fcfb.arceus.enums.system.MessageType
import com.fcfb.arceus.model.RequestMessageLog
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestMessageLogRepository : CrudRepository<RequestMessageLog, Int> {
    fun findByMessageType(messageType: MessageType): List<RequestMessageLog>

    fun findByGameId(gameId: Int): List<RequestMessageLog>

    fun findByPlayId(playId: Int): List<RequestMessageLog>

    fun findByMessageId(messageId: Long): List<RequestMessageLog>

    fun findByMessageLocation(messageLocation: String): List<RequestMessageLog>

    fun findByGameIdAndMessageType(
        gameId: Int,
        messageType: MessageType,
    ): List<RequestMessageLog>

    fun findByGameIdAndPlayId(
        gameId: Int,
        playId: Int,
    ): List<RequestMessageLog>

    fun findByGameIdAndMessageTypeAndPlayId(
        gameId: Int,
        messageType: MessageType,
        playId: Int,
    ): List<RequestMessageLog>
}
