package com.fcfb.arceus.domain

import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "audit_log", schema = "arceus")
@TypeDef(name = "json", typeClass = JsonStringType::class)
class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Int? = null

    @Basic
    @Column(name = "endpoint", nullable = false)
    var endpoint: String? = null

    @Basic
    @Column(name = "http_method", nullable = false)
    var httpMethod: String? = null

    @Type(type = "json")
    @Column(name = "body")
    var body: String? = null

    @Basic
    @Column(name = "transaction_date", nullable = false)
    var transactionDate: String? = null // Consider using LocalDateTime instead

    @Basic
    @Column(name = "username", nullable = false)
    var username: String? = null

    constructor(
        endpoint: String,
        httpMethod: String,
        body: String?,
        transactionDate: String,
        username: String,
    ) {
        this.endpoint = endpoint
        this.httpMethod = httpMethod
        this.body = body
        this.transactionDate = transactionDate
        this.username = username
    }

    constructor()
}
