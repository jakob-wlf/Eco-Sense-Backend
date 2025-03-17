package de.jakob.eco_sense.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import java.util.*


@Entity
@Table(name = "comments")
data class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    val id: String? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,

    @Column(nullable = false, length = 500)
    val content: String,

    @CreationTimestamp
    val createdAt: Timestamp? = null
)