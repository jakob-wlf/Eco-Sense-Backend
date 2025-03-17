package de.jakob.eco_sense.service

import de.jakob.eco_sense.exception.NotFoundException
import de.jakob.eco_sense.exception.PostTooLongException
import de.jakob.eco_sense.exception.PostTooShortException
import de.jakob.eco_sense.exception.UnauthorizedException
import de.jakob.eco_sense.model.Comment
import de.jakob.eco_sense.payload.request.CreateCommentRequest
import de.jakob.eco_sense.payload.rersponse.CommentResponse
import de.jakob.eco_sense.payload.rersponse.PostResponse
import de.jakob.eco_sense.repository.CommentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val userService: UserService,
    private val postService: PostService
) {

    fun addComment(token: String, id: String, createCommentRequest: CreateCommentRequest): CommentResponse {
        val user = userService.getUserEntityBySessionToken(token)

        val content = createCommentRequest.content
        if(content.length > 256) {
            throw PostTooLongException("Comment content exceeds allowed length")
        }

        if(content.isEmpty()) {
            throw PostTooShortException("Comment content must not be empty")
        }

        val post = postService.getPostEntity(id)

        val comment = Comment(
            user = user,
            post = post,
            content = content
        )

        commentRepository.save(comment)
        commentRepository.flush()

        return CommentResponse.fromEntity(comment)
    }

    fun getPaginatedComments(postId: String, page: Int, size: Int): Page<CommentResponse> {
        val sort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id"))
        val pageable = PageRequest.of(page, size, sort)

        return commentRepository.findAll(pageable)
            .map { CommentResponse.fromEntity(it) }
    }

    fun deleteComment(token: String, id: String) {
        val user = userService.getUserEntityBySessionToken(token)

        val comment = commentRepository.findById(id).orElseThrow { NotFoundException("Comment not found") }

        if(user.id != comment.user.id)
            throw UnauthorizedException("Comment does not belong to you")

        commentRepository.delete(comment)
        commentRepository.flush()
    }

}