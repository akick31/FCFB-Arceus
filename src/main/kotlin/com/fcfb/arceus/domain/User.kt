package com.fcfb.arceus.domain

import com.fcfb.arceus.domain.Game.DefensivePlaybook
import com.fcfb.arceus.domain.Game.OffensivePlaybook
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
@Table(name = "user", schema = "arceus")
class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    var id: Long? = 0

    @Basic
    @Column(name = "username")
    lateinit var username: String

    @Basic
    @Column(name = "coach_name")
    lateinit var coachName: String

    @Basic
    @Column(name = "discord_tag")
    lateinit var discordTag: String

    @Basic
    @Column(name = "discord_id")
    var discordId: String? = null

    @Basic
    @Column(name = "email")
    lateinit var email: String

    @Basic
    @Column(name = "password")
    lateinit var password: String

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "position")
    lateinit var position: CoachPosition

    @Basic
    @Column(name = "reddit_username")
    var redditUsername: String? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "role")
    var role: Role? = Role.USER

    @Basic
    @Column(name = "salt")
    lateinit var salt: String

    @Basic
    @Column(name = "team")
    var team: String? = null

    @Basic
    @Column(name = "wins")
    var wins: Int? = 0

    @Basic
    @Column(name = "losses")
    var losses: Int? = 0

    @Basic
    @Column(name = "win_percentage")
    var winPercentage: Double? = 0.0

    @Basic
    @Column(name = "offensive_playbook")
    var offensivePlaybook: OffensivePlaybook? = null

    @Basic
    @Column(name = "defensive_playbook")
    var defensivePlaybook: DefensivePlaybook? = null

    @Basic
    @Column(name = "approved")
    var approved: Byte? = 0

    @Basic
    @Column(name = "verification_token")
    lateinit var verificationToken: String

    constructor(
        username: String,
        coachName: String,
        discordTag: String,
        discordId: String?,
        email: String,
        password: String,
        position: CoachPosition,
        redditUsername: String?,
        role: Role,
        salt: String,
        team: String?,
        wins: Int,
        losses: Int,
        winPercentage: Double,
        offensivePlaybook: OffensivePlaybook?,
        defensivePlaybook: DefensivePlaybook?,
        approved: Byte,
        verificationToken: String
    ) {
        this.username = username
        this.coachName = coachName
        this.discordTag = discordTag
        this.discordId = discordId
        this.email = email
        this.password = password
        this.position = position
        this.redditUsername = redditUsername
        this.role = role
        this.salt = salt
        this.team = team
        this.wins = wins
        this.losses = losses
        this.winPercentage = winPercentage
        this.offensivePlaybook = offensivePlaybook
        this.defensivePlaybook = defensivePlaybook
        this.approved = approved
        this.verificationToken = verificationToken
    }

    constructor()

    enum class CoachPosition(val description: String) {
        HEAD_COACH("Head Coach"),
        OFFENSIVE_COORDINATOR("Offensive Coordinator"),
        DEFENSIVE_COORDINATOR("Defensive Coordinator"),
        RETIRED("Retired");

        companion object {
            fun fromString(description: String): CoachPosition? {
                return CoachPosition.values().find { it.description == description }
            }
        }
    }

    enum class Role(val description: String) {
        USER("User"),
        CONFERENCE_COMMISSIONER("Conference Commissioner"),
        ADMIN("Admin");

        companion object {
            fun fromString(description: String): Role? {
                return Role.values().find { it.description == description }
            }
        }
    }
}
