package de.jakob.eco_sense.model

import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "likes")
data class Like(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    val id: String? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post
)