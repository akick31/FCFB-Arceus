package com.fcfb.arceus.domain

import com.fcfb.arceus.converter.DefensivePlaybookConverter
import com.fcfb.arceus.converter.OffensivePlaybookConverter
import com.fcfb.arceus.converter.SubdivisionConverter
import com.fcfb.arceus.domain.Game.DefensivePlaybook
import com.fcfb.arceus.domain.Game.OffensivePlaybook
import com.fcfb.arceus.domain.Game.Subdivision
import java.util.Objects
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "team", schema = "arceus")
class Team {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    var id: Int? = 0

    @Basic
    @Column(name = "logo")
    var logo: String? = null

    @Basic
    @Column(name = "coach_username")
    var coachUsername: String? = null

    @Basic
    @Column(name = "coach_name")
    var coachName: String? = null

    @Basic
    @Column(name = "coach_discord_tag")
    var coachDiscordTag: String? = null

    @Basic
    @Column(name = "coach_discord_id")
    var coachDiscordId: String? = null

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

    @Convert(converter = SubdivisionConverter::class)
    @Column(name = "subdivision")
    var subdivision: Subdivision? = null

    @Convert(converter = OffensivePlaybookConverter::class)
    @Column(name = "offensive_playbook")
    var offensivePlaybook: OffensivePlaybook? = null

    @Convert(converter = DefensivePlaybookConverter::class)
    @Column(name = "defensive_playbook")
    var defensivePlaybook: DefensivePlaybook? = null

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
        logo: String?,
        coachUsername: String?,
        coachName: String?,
        coachDiscordTag: String?,
        coachDiscordId: String?,
        coachesPollRanking: Int?,
        name: String?,
        playoffCommitteeRanking: Int?,
        primaryColor: String?,
        secondaryColor: String?,
        subdivision: Subdivision?,
        offensivePlaybook: OffensivePlaybook?,
        defensivePlaybook: DefensivePlaybook?,
        conference: String?,
        currentWins: Int?,
        currentLosses: Int?,
        overallWins: Int?,
        overallLosses: Int?,
        currentConferenceWins: Int?,
        currentConferenceLosses: Int?,
        overallConferenceWins: Int?,
        overallConferenceLosses: Int?
    ) {
        this.logo = logo
        this.coachUsername = coachUsername
        this.coachName = coachName
        this.coachDiscordTag = coachDiscordTag
        this.coachDiscordId = coachDiscordId
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
        val that = o as Team
        return id == that.id && logo == that.logo && coachUsername == that.coachUsername && coachName == that.coachName && coachDiscordTag == that.coachDiscordTag && coachDiscordId == that.coachDiscordId && coachesPollRanking == that.coachesPollRanking && name == that.name && playoffCommitteeRanking == that.playoffCommitteeRanking && primaryColor == that.primaryColor && secondaryColor == that.secondaryColor && subdivision == that.subdivision && offensivePlaybook == that.offensivePlaybook && defensivePlaybook == that.defensivePlaybook && conference == that.conference && currentWins == that.currentWins && currentLosses == that.currentLosses && overallWins == that.overallWins && overallLosses == that.overallLosses && currentConferenceWins == that.currentConferenceWins && currentConferenceLosses == that.currentConferenceLosses && overallConferenceWins == that.overallConferenceWins && overallConferenceLosses == that.overallConferenceLosses
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            logo,
            coachUsername,
            coachName,
            coachDiscordTag,
            coachDiscordId,
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
}
