package de.jakob.eco_sense.service

import de.jakob.eco_sense.exception.NotFoundException
import de.jakob.eco_sense.exception.UnauthorizedException
import de.jakob.eco_sense.model.Conversation
import de.jakob.eco_sense.model.Message
import de.jakob.eco_sense.model.User
import de.jakob.eco_sense.payload.request.SendMessageRequest
import de.jakob.eco_sense.payload.request.StartConversationRequest
import de.jakob.eco_sense.payload.rersponse.ConversationResponse
import de.jakob.eco_sense.payload.rersponse.MessageResponse
import de.jakob.eco_sense.repository.ConversationRepository
import de.jakob.eco_sense.repository.MessageRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class MessagingService(
    private val messageRepository: MessageRepository,
    private val conversationRepository: ConversationRepository,
    private val userService: UserService
) {

    fun getPaginatedConversations(page: Int, size: Int, sessionToken: String): Page<ConversationResponse> {
        val user = userService.getUserEntityBySessionToken(sessionToken)
        val sort = Sort.by(Sort.Order.desc("lastUpdated"), Sort.Order.desc("id"))
        val pageable = PageRequest.of(page, size, sort)

        return conversationRepository.findByParticipantsContaining(user, pageable).map { ConversationResponse.fromEntity(it) }
    }

    fun startConversation(sessionToken: String, startConversationRequest: StartConversationRequest): ConversationResponse {
        val user = userService.getUserEntityBySessionToken(sessionToken)

        val participants: List<User> = listOf(user) + startConversationRequest.conversationParticipantIds.map { userService.getUserEntityById(it) }
        if(participants.contains(user))
            throw UnauthorizedException("Cannot add yourself as a participant")
        val conversation = Conversation(participants = participants)

        conversationRepository.save(conversation)
        conversationRepository.flush()

        return ConversationResponse.fromEntity(conversation)
    }

    fun getConversation(sessionToken: String, id: String): ConversationResponse {
        val user = userService.getUserEntityBySessionToken(sessionToken)
        val conversation = conversationRepository.findByIdAndParticipantsContaining(id, user).orElseThrow { NotFoundException("Conversation not found or the user does not participate") }

        return ConversationResponse.fromEntity(conversation)
    }

    fun sendMessage(sessionToken: String, id: String, sendMessageRequest: SendMessageRequest): MessageResponse {
        val user = userService.getUserEntityBySessionToken(sessionToken)
        val conversation = conversationRepository.findByIdAndParticipantsContaining(id, user).orElseThrow { NotFoundException("Conversation not found or the user does not participate") }

        val content = sendMessageRequest.content

        val message = Message(
            conversation = conversation,
            sender = user,
            content = content
        )

        messageRepository.save(message)

        return MessageResponse.fromEntity(message)
    }

    fun getPaginatedMessages(page: Int, size: Int, sessionToken: String, conversationId: String): Page<MessageResponse> {
        val user = userService.getUserEntityBySessionToken(sessionToken)
        val conversation = conversationRepository.findByIdAndParticipantsContaining(conversationId, user).orElseThrow { NotFoundException("Conversation not found or the user does not participate") }
        val sort = Sort.by(Sort.Order.desc("sentAt"), Sort.Order.desc("id"))
        val pageable = PageRequest.of(page, size, sort)

        return messageRepository.findByConversation(conversation, pageable).map { MessageResponse.fromEntity(it) }
    }

}