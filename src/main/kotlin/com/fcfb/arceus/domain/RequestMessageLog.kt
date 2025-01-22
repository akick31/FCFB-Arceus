package com.fcfb.arceus.domain

import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.TypeDef
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "request_message_log", schema = "arceus")
@TypeDef(name = "json", typeClass = JsonStringType::class)
class RequestMessageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    @Basic
    @Column(name = "message_type", nullable = false)
    var messageType: MessageType? = null

    @Basic
    @Column(name = "game_id", nullable = false)
    var gameId: String? = null

    @Basic
    @Column(name = "play_id")
    var playId: String? = null

    @Basic
    @Column(name = "message_id", nullable = false)
    var messageId: Int? = null

    @Basic
    @Column(name = "message_content", nullable = false)
    var messageContent: String? = null

    @Basic
    @Column(name = "message_location", nullable = false)
    var messageLocation: String? = null

    @Basic
    @Column(name = "message_ts", nullable = false)
    var messageTs: String? = null

    constructor(
        messageType: MessageType,
        gameId: String,
        playId: String?,
        messageId: Int,
        messageContent: String,
        messageLocation: String,
        messageTs: String?,
    ) {
        this.messageType = messageType
        this.gameId = gameId
        this.playId = playId
        this.messageId = messageId
        this.messageContent = messageContent
        this.messageLocation = messageLocation
        this.messageTs = messageTs
    }

    constructor()

    enum class MessageType(val description: String) {
        OFFENSE("Offense"),
        DEFENSE("Defense"),
    }
}
