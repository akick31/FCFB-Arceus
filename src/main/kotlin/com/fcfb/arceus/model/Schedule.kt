package com.fcfb.arceus.model

import com.fcfb.arceus.enums.game.GameType
import com.fcfb.arceus.enums.game.TVChannel
import com.fcfb.arceus.enums.team.Subdivision
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "schedule", schema = "arceus")
class Schedule {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    var id: Int = 0

    @Basic
    @Column(name = "season")
    var season: Int = 0

    @Basic
    @Column(name = "week")
    var week: Int = 0

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "subdivision")
    lateinit var subdivision: Subdivision

    @Basic
    @Column(name = "home_team")
    lateinit var homeTeam: String

    @Basic
    @Column(name = "away_team")
    lateinit var awayTeam: String

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "tv_channel")
    var tvChannel: TVChannel? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "game_type")
    lateinit var gameType: GameType

    @Basic
    @Column(name = "started")
    var started: Boolean? = false

    @Basic
    @Column(name = "finished")
    var finished: Boolean? = false
}
