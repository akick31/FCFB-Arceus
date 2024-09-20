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
@Table(name = "game_writeup", schema = "arceus")
class GameWriteup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    var id = 0

    @Basic
    @Column(name = "scenario")
    var scenario: String? = null

    @Basic
    @Column(name = "pass_or_run")
    var passOrRun: String? = null

    @Basic
    @Column(name = "message")
    var message: String? = null

    constructor(
        scenario: String?,
        passOrRun: String?,
        message: String?
    ) {
        this.scenario = scenario
        this.passOrRun = passOrRun
        this.message = message
    }

    constructor()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as GameWriteup
        return id == that.id && scenario == that.scenario && passOrRun == that.passOrRun && message == that.message
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            scenario,
            passOrRun,
            message
        )
    }
}
