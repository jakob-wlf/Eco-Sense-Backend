package de.jakob.eco_sense.controller

import de.jakob.eco_sense.payload.request.CreateCommentRequest
import de.jakob.eco_sense.payload.rersponse.CommentResponse
import de.jakob.eco_sense.service.CommentService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/posts/{id}/comments")
    fun createComment(@RequestHeader("Authorization") token: String, @PathVariable id: String, @RequestBody createCommentRequest: CreateCommentRequest): ResponseEntity<CommentResponse> {
        return ResponseEntity.ok(commentService.addComment(token, id, createCommentRequest))
    }

    @GetMapping("/posts/{id}/comments")
    fun getCommentsForPost(@PathVariable id: String, @RequestParam(defaultValue = "0") page: Int, @RequestParam(defaultValue = "10") size: Int): Page<CommentResponse> {
        return commentService.getPaginatedComments(id, page, size)
    }

    @DeleteMapping("/comments/{id}")
    fun deleteComment(@RequestHeader("Authorization") token: String, @PathVariable id: String): ResponseEntity<String> {
        commentService.deleteComment(token, id)
        return ResponseEntity.ok("Successfully deleted comment")
    }

}