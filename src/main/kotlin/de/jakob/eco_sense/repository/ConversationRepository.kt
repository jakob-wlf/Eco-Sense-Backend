package de.jakob.eco_sense.repository

import de.jakob.eco_sense.model.Conversation
import de.jakob.eco_sense.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ConversationRepository : JpaRepository<Conversation, String> {
    // Find conversations involving a specific user, paginated
    fun findByParticipantsContaining(user: User, pageable: Pageable): Page<Conversation>

    // Find a conversation by its ID
    fun findByIdAndParticipantsContaining(id: String, user: User): Optional<Conversation>
}