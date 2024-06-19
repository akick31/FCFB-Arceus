package com.fcfb.arceus.domain

import java.util.Objects
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "game_messages", schema = "arceus")
class GameMessagesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    var id = 0

    @Basic
    @Column(name = "scenario")
    var scenario: String? = null

    @Basic
    @Column(name = "message")
    var message: String? = null

    constructor(
        scenario: String?,
        message: String?
    ) {
        this.scenario = scenario
        this.message = message
    }

    constructor()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as GameMessagesEntity
        return id == that.id && scenario == that.scenario && message == that.message
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            scenario,
            message
        )
    }
}
