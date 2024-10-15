package com.fcfb.arceus.domain

import com.fcfb.arceus.converter.DefensivePlaybookConverter
import com.fcfb.arceus.converter.OffensivePlaybookConverter
import com.fcfb.arceus.converter.SubdivisionConverter
import com.fcfb.arceus.domain.Game.DefensivePlaybook
import com.fcfb.arceus.domain.Game.OffensivePlaybook
import com.fcfb.arceus.domain.Game.Subdivision
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
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
    @Column(name = "coach_username1")
    var coachUsername1: String? = null

    @Basic
    @Column(name = "coach_name1")
    var coachName1: String? = null

    @Basic
    @Column(name = "coach_discord_tag1")
    var coachDiscordTag1: String? = null

    @Basic
    @Column(name = "coach_discord_id1")
    var coachDiscordId1: String? = null

    @Basic
    @Column(name = "coach_username2")
    var coachUsername2: String? = null

    @Basic
    @Column(name = "coach_name2")
    var coachName2: String? = null

    @Basic
    @Column(name = "coach_discord_tag2")
    var coachDiscordTag2: String? = null

    @Basic
    @Column(name = "coach_discord_id2")
    var coachDiscordId2: String? = null

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
    @Column(name = "abbreviation")
    var abbreviation: String? = null

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

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "conference")
    var conference: Conference? = null

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
        coachUsername1: String?,
        coachName1: String?,
        coachDiscordTag1: String?,
        coachDiscordId1: String?,
        coachUsername2: String?,
        coachName2: String?,
        coachDiscordTag2: String?,
        coachDiscordId2: String?,
        coachesPollRanking: Int?,
        name: String?,
        playoffCommitteeRanking: Int?,
        abbreviation: String?,
        primaryColor: String?,
        secondaryColor: String?,
        subdivision: Subdivision?,
        offensivePlaybook: OffensivePlaybook?,
        defensivePlaybook: DefensivePlaybook?,
        conference: Conference?,
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
        this.coachUsername1 = coachUsername1
        this.coachName1 = coachName1
        this.coachDiscordTag1 = coachDiscordTag1
        this.coachDiscordId1 = coachDiscordId1
        this.coachUsername2 = coachUsername2
        this.coachName2 = coachName2
        this.coachDiscordTag2 = coachDiscordTag2
        this.coachDiscordId2 = coachDiscordId2
        this.coachesPollRanking = coachesPollRanking
        this.name = name
        this.playoffCommitteeRanking = playoffCommitteeRanking
        this.abbreviation = abbreviation
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

    enum class Conference(val description: String) {
        ACC("ACC"),
        AMERICAN("American"),
        BIG_12("Big 12"),
        BIG_TEN("Big Ten"),
        CUSA("C-USA"),
        FBS_INDEPENDENT("FBS Independent"),
        MAC("MAC"),
        MOUNTAIN_WEST("Mountain West"),
        PAC_12("Pac-12"),
        SEC("SEC"),
        SUN_BELT("Sun Belt"),
        ATLANTIC_SUN("Atlantic Sun"),
        BIG_SKY("Big Sky"),
        CAROLINA_FOOTBALL_CONFERENCE("Carolina Football Conference"),
        MISSOURI_VALLEY("Missouri Valley"),
        COLONIAL("Colonial"),
        NEC("NEC"),
        IVY_LEAGUE("Ivy League"),
        MID_ATLANTIC("Mid-Atlantic"),
        SOUTHLAND("Southland"),
        OHIO_VALLEY("Ohio Valley"),
        SWAC("SWAC");

        companion object {
            fun fromString(description: String): Conference? {
                return Conference.values().find { it.description == description }
            }
        }
    }
}
