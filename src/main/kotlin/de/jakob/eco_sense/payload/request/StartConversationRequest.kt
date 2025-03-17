package de.jakob.eco_sense.payload.request

data class StartConversationRequest(
    val conversationParticipantIds: List<String>
)