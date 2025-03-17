package de.jakob.eco_sense.model

import jakarta.persistence.*
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "credentials")
data class Credentials(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    val id: String? = null,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    val passwordHash: String,

    @UpdateTimestamp
    val lastUpdated: Timestamp? = null
)
