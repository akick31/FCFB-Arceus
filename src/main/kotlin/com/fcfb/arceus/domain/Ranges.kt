package com.fcfb.arceus.domain

import com.fcfb.arceus.domain.Game.Result
import java.util.Objects
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "ranges", schema = "arceus")
class Ranges {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    var id: Int? = 0

    @Basic
    @Column(name = "play_type")
    var playType: String? = null

    @Basic
    @Column(name = "offensive_playbook")
    var offensivePlaybook: String? = null

    @Basic
    @Column(name = "defensive_playbook")
    var defensivePlaybook: String? = null

    @Basic
    @Column(name = "result")
    var result: Result? = null

    @Basic
    @Column(name = "play_time")
    var playTime: Int? = null

    @Basic
    @Column(name = "lower_range")
    var lowerRange: Int? = null

    @Basic
    @Column(name = "upper_range")
    var upperRange: Int? = null

    constructor(
        playType: String?,
        offensivePlaybook: String?,
        defensivePlaybook: String?,
        result: Result?,
        playTime: Int?,
        lowerRange: Int?,
        upperRange: Int?
    ) {
        this.playType = playType
        this.offensivePlaybook = offensivePlaybook
        this.defensivePlaybook = defensivePlaybook
        this.result = result
        this.playTime = playTime
        this.lowerRange = lowerRange
        this.upperRange = upperRange
    }

    constructor()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as Ranges
        return id == that.id && playType == that.playType && offensivePlaybook == that.offensivePlaybook && defensivePlaybook == that.defensivePlaybook && result == that.result && playTime == that.playTime && lowerRange == that.lowerRange && upperRange == that.upperRange
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            playType,
            offensivePlaybook,
            defensivePlaybook,
            result,
            playTime,
            lowerRange,
            upperRange
        )
    }
}
