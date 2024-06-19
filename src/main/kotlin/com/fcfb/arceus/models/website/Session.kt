// src/main/kotlin/com/fcfb/arceus/models/Session.kt

package com.fcfb.arceus.models.website

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "sessions")
data class Session(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "user_id")
    val userId: Long,
    val token: String,
    @Column(name = "expiration_time")
    val expirationTime: LocalDateTime
) {
    constructor(userId: Long, token: String, expirationTime: LocalDateTime) : this(0, userId, token, expirationTime)
    constructor() : this(0, 0, "", LocalDateTime.now())
}
