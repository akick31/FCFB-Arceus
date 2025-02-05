package com.fcfb.arceus.domain

import com.fcfb.arceus.converter.DefensivePlaybookConverter
import com.fcfb.arceus.converter.OffensivePlaybookConverter
import com.fcfb.arceus.converter.SubdivisionConverter
import com.fcfb.arceus.domain.Game.DefensivePlaybook
import com.fcfb.arceus.domain.Game.OffensivePlaybook
import com.fcfb.arceus.domain.Game.Subdivision
import org.hibernate.annotations.Type
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
    @Column(name = "name")
    var name: String? = null

    @Basic
    @Column(name = "abbreviation")
    var abbreviation: String? = null

    @Basic
    @Column(name = "short_name")
    var shortName: String? = null

    @Basic
    @Column(name = "logo")
    var logo: String? = null

    @Basic
    @Column(name = "scorebug_logo")
    var scorebugLogo: String? = null

    @Type(type = "json")
    @Column(name = "coach_usernames")
    var coachUsernames: MutableList<String>? = mutableListOf()

    @Type(type = "json")
    @Column(name = "coach_names")
    var coachNames: MutableList<String>? = mutableListOf()

    @Type(type = "json")
    @Column(name = "coach_discord_tags")
    var coachDiscordTags: MutableList<String>? = mutableListOf()

    @Type(type = "json")
    @Column(name = "coach_discord_ids")
    var coachDiscordIds: MutableList<String>? = mutableListOf()

    @Basic
    @Column(name = "primary_color")
    var primaryColor: String? = null

    @Basic
    @Column(name = "secondary_color")
    var secondaryColor: String? = null

    @Basic
    @Column(name = "coaches_poll_ranking")
    var coachesPollRanking: Int? = null

    @Basic
    @Column(name = "playoff_committee_ranking")
    var playoffCommitteeRanking: Int? = null

    @Convert(converter = SubdivisionConverter::class)
    @Column(name = "subdivision")
    var subdivision: Subdivision? = null

    @Convert(converter = OffensivePlaybookConverter::class)
    @Column(name = "offensive_playbook")
    lateinit var offensivePlaybook: OffensivePlaybook

    @Convert(converter = DefensivePlaybookConverter::class)
    @Column(name = "defensive_playbook")
    lateinit var defensivePlaybook: DefensivePlaybook

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "conference")
    var conference: Conference? = null

    @Basic
    @Column(name = "current_wins")
    var currentWins: Int = 0

    @Basic
    @Column(name = "current_losses")
    var currentLosses: Int = 0

    @Basic
    @Column(name = "overall_wins")
    var overallWins: Int = 0

    @Basic
    @Column(name = "overall_losses")
    var overallLosses: Int = 0

    @Basic
    @Column(name = "current_conference_wins")
    var currentConferenceWins: Int = 0

    @Basic
    @Column(name = "current_conference_losses")
    var currentConferenceLosses: Int = 0

    @Basic
    @Column(name = "overall_conference_wins")
    var overallConferenceWins: Int = 0

    @Basic
    @Column(name = "overall_conference_losses")
    var overallConferenceLosses: Int = 0

    @Basic
    @Column(name = "conference_championship_wins")
    var conferenceChampionshipWins: Int = 0

    @Basic
    @Column(name = "conference_championship_losses")
    var conferenceChampionshipLosses: Int = 0

    @Basic
    @Column(name = "bowl_wins")
    var bowlWins: Int = 0

    @Basic
    @Column(name = "bowl_losses")
    var bowlLosses: Int = 0

    @Basic
    @Column(name = "playoff_wins")
    var playoffWins: Int = 0

    @Basic
    @Column(name = "playoff_losses")
    var playoffLosses: Int = 0

    @Basic
    @Column(name = "national_championship_wins")
    var nationalChampionshipWins: Int = 0

    @Basic
    @Column(name = "national_championship_losses")
    var nationalChampionshipLosses: Int = 0

    @Basic
    @Column(name = "active")
    var active: Boolean = true

    constructor(
        logo: String?,
        scorebugLogo: String?,
        coachUsernames: MutableList<String>,
        coachNames: MutableList<String>,
        coachDiscordTags: MutableList<String>,
        coachDiscordIds: MutableList<String>,
        name: String?,
        shortName: String?,
        abbreviation: String?,
        primaryColor: String?,
        secondaryColor: String?,
        coachesPollRanking: Int?,
        playoffCommitteeRanking: Int?,
        subdivision: Subdivision?,
        offensivePlaybook: OffensivePlaybook,
        defensivePlaybook: DefensivePlaybook,
        conference: Conference?,
        currentWins: Int,
        currentLosses: Int,
        overallWins: Int,
        overallLosses: Int,
        currentConferenceWins: Int,
        currentConferenceLosses: Int,
        overallConferenceWins: Int,
        overallConferenceLosses: Int,
        conferenceChampionshipWins: Int,
        conferenceChampionshipLosses: Int,
        bowlWins: Int,
        bowlLosses: Int,
        playoffWins: Int,
        playoffLosses: Int,
        nationalChampionshipWins: Int,
        nationalChampionshipLosses: Int,
        active: Boolean,
    ) {
        this.logo = logo
        this.scorebugLogo = scorebugLogo
        this.coachUsernames = coachUsernames
        this.coachNames = coachNames
        this.coachDiscordTags = coachDiscordTags
        this.coachDiscordIds = coachDiscordIds
        this.name = name
        this.shortName = shortName
        this.abbreviation = abbreviation
        this.primaryColor = primaryColor
        this.secondaryColor = secondaryColor
        this.coachesPollRanking = coachesPollRanking
        this.playoffCommitteeRanking = playoffCommitteeRanking
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
        this.conferenceChampionshipWins = conferenceChampionshipWins
        this.conferenceChampionshipLosses = conferenceChampionshipLosses
        this.bowlWins = bowlWins
        this.bowlLosses = bowlLosses
        this.playoffWins = playoffWins
        this.playoffLosses = playoffLosses
        this.nationalChampionshipWins = nationalChampionshipWins
        this.nationalChampionshipLosses = nationalChampionshipLosses
        this.active = active
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
        SWAC("SWAC"),
        ;

        companion object {
            fun fromString(description: String): Conference? {
                return Conference.values().find { it.description == description }
            }
        }
    }
}
