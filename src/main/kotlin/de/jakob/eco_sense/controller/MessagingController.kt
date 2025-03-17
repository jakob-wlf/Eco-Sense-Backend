package de.jakob.eco_sense.controller

import de.jakob.eco_sense.payload.request.SendMessageRequest
import de.jakob.eco_sense.payload.request.StartConversationRequest
import de.jakob.eco_sense.payload.rersponse.ConversationResponse
import de.jakob.eco_sense.payload.rersponse.MessageResponse
import de.jakob.eco_sense.service.MessagingService
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/conversations")
class MessagingController(
    private val messagingService: MessagingService
){

    @GetMapping
    fun getConversations(
        @RequestHeader("Authorization") token: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<ConversationResponse> {
        return messagingService.getPaginatedConversations(page, size, token)
    }

    @PostMapping
    fun startConversation(@RequestHeader("Authorization") token: String, @RequestBody startConversationRequest: StartConversationRequest): ConversationResponse {
        return messagingService.startConversation(token, startConversationRequest)
    }

    @GetMapping("/{id}")
    fun getConversation(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String
    ): ConversationResponse {
        return messagingService.getConversation(token, id)
    }

    @PostMapping("/{id}/messages")
    fun sendMessage(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String,
        @RequestBody sendMessageRequest: SendMessageRequest
    ): MessageResponse {
        return messagingService.sendMessage(token, id, sendMessageRequest)
    }

    @GetMapping("/{id}/messages")
    fun getMessages(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): Page<MessageResponse> {
        return messagingService.getPaginatedMessages(page, size, token, id)
    }

}