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
    @Column(name = "losses")
    var losses: Int? = 0

    @Basic
    @Column(name = "password")
    lateinit var password: String

    @Basic
    @Column(name = "position")
    lateinit var position: String

    @Basic
    @Column(name = "reddit_username")
    var redditUsername: String? = null

    @Basic
    @Column(name = "role")
    var role: String? = "user"

    @Basic
    @Column(name = "salt")
    lateinit var salt: String

    @Basic
    @Column(name = "team")
    var team: String? = null

    @Basic
    @Column(name = "win_percentage")
    var winPercentage: Double? = 0.0

    @Basic
    @Column(name = "wins")
    var wins: Int? = 0

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
        losses: Int,
        password: String,
        position: String,
        redditUsername: String?,
        role: String,
        salt: String,
        team: String?,
        winPercentage: Double,
        wins: Int,
        approved: Byte,
        verificationToken: String
    ) {
        this.username = username
        this.coachName = coachName
        this.discordTag = discordTag
        this.discordId = discordId
        this.email = email
        this.losses = losses
        this.password = password
        this.position = position
        this.redditUsername = redditUsername
        this.role = role
        this.salt = salt
        this.team = team
        this.winPercentage = winPercentage
        this.wins = wins
        this.approved = approved
        this.verificationToken = verificationToken
    }

    constructor()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as User
        return id == that.id && username === that.username && approved == that.approved && coachName == that.coachName && discordTag == that.discordTag && discordId == that.discordId && email == that.email && losses == that.losses && password == that.password && position == that.position && redditUsername == that.redditUsername && role == that.role && salt == that.salt && team == that.team && winPercentage == that.winPercentage && wins == that.wins
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            username,
            coachName,
            discordTag,
            discordId,
            email,
            losses,
            password,
            position,
            redditUsername,
            role,
            salt,
            team,
            winPercentage,
            wins,
            approved,
            verificationToken
        )
    }
}
