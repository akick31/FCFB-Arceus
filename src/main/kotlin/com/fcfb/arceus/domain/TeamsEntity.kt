package com.fcfb.arceus.domain

import java.util.*
import jakarta.persistence.*

@Entity
@Table(name = "teams", schema = "arceus")
class TeamsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    var id = 0

    @Basic
    @Column(name = "logo")
    var logo: String? = null

    @Basic
    @Column(name = "coach")
    var coach: String? = null

    @Basic
    @Column(name = "coaches_poll_ranking")
    var coachesPollRanking: Int? = null

    @Basic
    @Column(name = "name")
    var name: String? = null

    @Basic
    @Column(name = "playoff_committee_ranking")
    var playoffCommitteeRanking: Int? = null

    @Basic
    @Column(name = "primary_color")
    var primaryColor: String? = null

    @Basic
    @Column(name = "secondary_color")
    var secondaryColor: String? = null

    @Basic
    @Column(name = "subdivision")
    var subdivision: String? = null

    @Basic
    @Column(name = "offensive_playbook")
    var offensivePlaybook: String? = null

    @Basic
    @Column(name = "defensive_playbook")
    var defensivePlaybook: String? = null

    @Basic
    @Column(name = "conference")
    var conference: String? = null

    @Basic
    @Column(name = "current_wins")
    var currentWins: Int? = null

    @Basic
    @Column(name = "current_losses")
    var currentLosses: Int? = null

    @Basic
    @Column(name = "overall_wins")
    var overallWins: Int? = null

    @Basic
    @Column(name = "overall_losses")
    var overallLosses: Int? = null

    @Basic
    @Column(name = "current_conference_wins")
    var currentConferenceWins: Int? = null

    @Basic
    @Column(name = "current_conference_losses")
    var currentConferenceLosses: Int? = null

    @Basic
    @Column(name = "overall_conference_wins")
    var overallConferenceWins: Int? = null

    @Basic
    @Column(name = "overall_conference_losses")
    var overallConferenceLosses: Int? = null

    constructor(
        logo: String?, coach: String?, coachesPollRanking: Int?, name: String?,
        playoffCommitteeRanking: Int?, primaryColor: String?, secondaryColor: String?, subdivision: String?,
        offensivePlaybook: String?, defensivePlaybook: String?, conference: String?, currentWins: Int?,
        currentLosses: Int?, overallWins: Int?, overallLosses: Int?, currentConferenceWins: Int?,
        currentConferenceLosses: Int?, overallConferenceWins: Int?,
        overallConferenceLosses: Int?
    ) {
        this.logo = logo
        this.coach = coach
        this.coachesPollRanking = coachesPollRanking
        this.name = name
        this.playoffCommitteeRanking = playoffCommitteeRanking
        this.primaryColor = primaryColor
        this.secondaryColor = secondaryColor
        this.subdivision = subdivision
        this.offensivePlaybook = offensivePlaybook
        this.defensivePlaybook = defensivePlaybook
        this.conference = conference
        this.currentWins = currentWins
        this.currentLosses = currentLosses
        this.overallWins = overallWins
        this.overallLosses = overallLosses
        this.currentConferenceWins = currentConferenceWins
        this.currentConferenceLosses = currentConferenceLosses
        this.overallConferenceWins = overallConferenceWins
        this.overallConferenceLosses = overallConferenceLosses
    }

    constructor()



    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as TeamsEntity
        return id == that.id && logo == that.logo && coach == that.coach && coachesPollRanking == that.coachesPollRanking && name == that.name && playoffCommitteeRanking == that.playoffCommitteeRanking && primaryColor == that.primaryColor && secondaryColor == that.secondaryColor && subdivision == that.subdivision && offensivePlaybook == that.offensivePlaybook && defensivePlaybook == that.defensivePlaybook && conference == that.conference && currentWins == that.currentWins && currentLosses == that.currentLosses && overallWins == that.overallWins && overallLosses == that.overallLosses && currentConferenceWins == that.currentConferenceWins && currentConferenceLosses == that.currentConferenceLosses && overallConferenceWins == that.overallConferenceWins && overallConferenceLosses == that.overallConferenceLosses
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            logo,
            coach,
            coachesPollRanking,
            name,
            playoffCommitteeRanking,
            primaryColor,
            secondaryColor,
            subdivision,
            offensivePlaybook,
            defensivePlaybook,
            conference,
            currentWins,
            currentLosses,
            overallWins,
            overallLosses,
            currentConferenceWins,
            currentConferenceLosses,
            overallConferenceWins,
            overallConferenceLosses
        )
    }

    override fun toString(): String {
        return """{
          "id": "$id",
          "logo": "$logo",
          "coach": "$coach",
          "coachesPollRanking": $coachesPollRanking,
          "name": "$name",
          "playoffCommitteeRanking": $playoffCommitteeRanking,
          "primaryColor": "$primaryColor",
          "secondaryColor": "$secondaryColor",
          "subdivision": "$subdivision",
          "offensivePlaybook": "$offensivePlaybook",
          "defensivePlaybook": "$defensivePlaybook",
          "conference": "$conference",
          "currentWins": $currentWins,
          "currentLosses": $currentLosses,
          "overallWins": $overallWins,
          "overallLosses": $overallLosses,
          "currentConferenceWins": $currentConferenceWins,
          "currentConferenceLosses": $currentConferenceLosses,
          "overallConferenceWins": $overallConferenceWins,
          "overallConferenceLosses": $overallConferenceLosses
        }"""
    }
}
