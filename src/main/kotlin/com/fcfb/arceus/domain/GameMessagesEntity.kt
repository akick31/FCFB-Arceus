package com.fcfb.arceus.domain

import com.fcfb.arceus.models.GameScenario
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "game_messages", schema = "arceus")
class GameMessagesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    var id = 0

    @Basic
    @Column(name = "scenario")
    var scenario: GameScenario? = null

    @Basic
    @Column(name = "message")
    var message: String? = null

    constructor(
        scenario: GameScenario,
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
