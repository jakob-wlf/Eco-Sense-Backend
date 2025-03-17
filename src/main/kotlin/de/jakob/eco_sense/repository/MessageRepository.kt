package de.jakob.eco_sense.repository

import de.jakob.eco_sense.model.Conversation
import de.jakob.eco_sense.model.Message
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, String> {
    fun findByConversation(conversation: Conversation, pageable: Pageable): Page<Message>
}