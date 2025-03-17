package de.jakob.eco_sense.payload.rersponse

import de.jakob.eco_sense.model.Message

data class MessageResponse(
    val id: String,
    val content: String,
    val sender: String,  // Assuming sender's username for simplicity
    val sentAt: String
) {
    companion object {
        fun fromEntity(message: Message): MessageResponse {
            return MessageResponse(
                id = message.id ?: "No id assigned yet",
                content = message.content,
                sender = message.sender.username,
                sentAt = message.sentAt.toString()
            )
        }
    }
}