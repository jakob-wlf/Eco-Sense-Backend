package de.jakob.eco_sense.model

import jakarta.persistence.*
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "carbon_footprints")
data class CarbonFootprint(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    val id: String? = null,

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    val totalFootprint: Double,
    val transportFootprint: Double,
    val energyFootprint: Double,
    val foodFootprint: Double,

    @UpdateTimestamp
    val calculatedAt: Timestamp? = null
)