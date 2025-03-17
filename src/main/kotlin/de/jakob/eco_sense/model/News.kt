package de.jakob.eco_sense.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "news")
data class News(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    val id: String? = null,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false, length = 2000)
    val content: String,

    val sourceUrl: String? = null,

    @CreationTimestamp
    val publishedAt: Timestamp? = null
)
