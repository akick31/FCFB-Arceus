package com.fcfb.arceus.domain

import com.fcfb.arceus.domain.Game.Subdivision
import com.fcfb.arceus.domain.User.CoachPosition
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "coach_transaction_log", schema = "arceus")
@TypeDef(name = "json", typeClass = JsonStringType::class)
class CoachTransactionLog {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    @Basic
    @Column(name = "team", nullable = false)
    var team: String? = null

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "position", nullable = false)
    var position: CoachPosition? = null

    @Type(type = "json")
    @Column(name = "coach", nullable = false)
    var coach: MutableList<String>? = mutableListOf()

    @Enumerated(EnumType.STRING)
    @Basic
    @Column(name = "transaction", nullable = false)
    var transaction: TransactionType? = null

    @Basic
    @Column(name = "transaction_date", nullable = false)
    var transactionDate: String? = null

    @Basic
    @Column(name = "processed_by", nullable = false)
    var processedBy: String? = null

    constructor(
        team: String,
        position: CoachPosition,
        coach: MutableList<String>? = mutableListOf(),
        transaction: TransactionType,
        transactionDate: String,
        processedBy: String,
    ) {
        this.team = team
        this.position = position
        this.coach = coach
        this.transaction = transaction
        this.transactionDate = transactionDate
        this.processedBy = processedBy
    }

    constructor()

    enum class TransactionType(val description: String) {
        HIRED("Hired"),
        FIRED("Fired"),
        ;

        companion object {
            fun fromString(description: String): TransactionType? {
                return TransactionType.entries.find { it.description == description }
            }
        }
    }
}

