package com.fcfb.arceus.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "ranges", schema = "arceus")
class RangesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    var id = 0

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
    var result: String? = null

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
        result: String?,
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
        val that = o as RangesEntity
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
