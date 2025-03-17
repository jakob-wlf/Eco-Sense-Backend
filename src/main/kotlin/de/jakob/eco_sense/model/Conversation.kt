package de.jakob.eco_sense.model

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp

@Entity
@Table(name = "conversations")
data class Conversation(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    val id: String? = null,

    @ManyToMany
    @JoinTable(
        name = "conversation_participants",
        joinColumns = [JoinColumn(name = "conversation_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val participants: List<User>,

    @CreationTimestamp
    val createdAt: Timestamp? = null,

    @UpdateTimestamp
    val lastUpdated: Timestamp? = null,

    @Column(name = "last_message_sent_at")
    var lastMessageSentAt: Timestamp? = null
)