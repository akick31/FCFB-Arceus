package com.fcfb.arceus.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "users", schema = "arceus")
class UsersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    var id: Long = 0

    @Basic
    @Column(name = "username")
    var username: String? = null

    @Basic
    @Column(name = "coach_name")
    var coachName: String? = null

    @Basic
    @Column(name = "discord_tag")
    var discordTag: String? = null

    @Basic
    @Column(name = "email")
    var email: String? = null

    @Basic
    @Column(name = "losses")
    var losses: Int? = null

    @Basic
    @Column(name = "password")
    var password: String? = null

    @Basic
    @Column(name = "position")
    var position: String? = null

    @Basic
    @Column(name = "reddit_username")
    var redditUsername: String? = null

    @Basic
    @Column(name = "role")
    var role: String? = null

    @Basic
    @Column(name = "salt")
    var salt: String? = null

    @Basic
    @Column(name = "team")
    var team: String? = null

    @Basic
    @Column(name = "win_percentage")
    var winPercentage: Double? = null

    @Basic
    @Column(name = "wins")
    var wins: Int? = null

    @Basic
    @Column(name = "approved")
    var approved: Byte = 0

    constructor(
        username: String?,
        coachName: String?,
        discordTag: String?,
        email: String?,
        losses: Int?,
        password: String?,
        position: String?,
        redditUsername: String?,
        role: String?,
        salt: String?,
        team: String?,
        winPercentage: Double?,
        wins: Int?,
        approved: Byte
    ) {
        this.username = username
        this.coachName = coachName
        this.discordTag = discordTag
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
    }

    constructor()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as UsersEntity
        return id == that.id && username === that.username && approved == that.approved && coachName == that.coachName && discordTag == that.discordTag && email == that.email && losses == that.losses && password == that.password && position == that.position && redditUsername == that.redditUsername && role == that.role && salt == that.salt && team == that.team && winPercentage == that.winPercentage && wins == that.wins
    }

    override fun hashCode(): Int {
        return Objects.hash(
            id,
            username,
            coachName,
            discordTag,
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
            approved
        )
    }
}
