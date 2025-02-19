package com.fcfb.arceus.domain

import com.fcfb.arceus.domain.Game.ActualResult
import com.fcfb.arceus.domain.Game.PlayCall
import com.fcfb.arceus.domain.Game.Scenario
import com.fcfb.arceus.domain.Game.TeamSide
import java.math.BigInteger
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "drive", schema = "arceus")
class Drive {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "drive_id")
    var driveId: Int = 0

    @Basic
    @Column(name = "game_id")
    var gameId: Int = 0

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "possession")
    var possession: TeamSide = TeamSide.HOME

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "result")
    var result: ActualResult? = null

    @Basic
    @Column(name = "drive_finished")
    var driveFinished: Boolean = false

    constructor(
        gameId: Int,
        possession: TeamSide,
        result: ActualResult?,
        driveFinished: Boolean,
    ) {
        this.gameId = gameId
        this.possession = possession
        this.result = result
        this.driveFinished = driveFinished
    }

    constructor()
}
