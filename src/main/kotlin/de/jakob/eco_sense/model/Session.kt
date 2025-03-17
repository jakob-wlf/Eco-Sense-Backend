package de.jakob.eco_sense.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "sessions")
data class Session(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    val id: String? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false, unique = true)
    val sessionToken: String,

    val ipAddress: String? = null,
    val deviceInfo: String? = null,

    @CreationTimestamp
    val createdAt: Timestamp? = null,

    val expiresAt: Timestamp? = null
)