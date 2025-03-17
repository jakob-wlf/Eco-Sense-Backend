package de.jakob.eco_sense.payload.rersponse

import de.jakob.eco_sense.model.Comment

data class CommentResponse(
    val commentId: String,
    val content: String,
    val createdAt: String,
    val userId: String,
    val username: String
) {
    companion object {
        fun fromEntity(
            comment: Comment
        ): CommentResponse {
            return CommentResponse(
                commentId = comment.id!!,
                content = comment.content,
                createdAt = comment.createdAt.toString(),
                userId = comment.user.id!!,
                username = comment.user.username
            )
        }
    }
}