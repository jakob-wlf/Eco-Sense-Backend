package de.jakob.eco_sense.payload.rersponse

import de.jakob.eco_sense.model.Conversation

data class ConversationResponse(
    val id: String,
    val participants: List<String>,  // Assuming usernames for simplicity
    val createdAt: String,
    val lastUpdated: String,
    val lastMessageSentAt: String?  // Nullable if no messages
) {
    companion object {
        fun fromEntity(conversation: Conversation): ConversationResponse {
            val participantUsernames = conversation.participants.map { it.username }
            return ConversationResponse(
                id = conversation.id ?: "No id assigned yet",
                participants = participantUsernames,
                createdAt = conversation.createdAt.toString(),
                lastUpdated = conversation.lastUpdated.toString(),
                lastMessageSentAt = conversation.lastMessageSentAt?.toString()
            )
        }
    }
}