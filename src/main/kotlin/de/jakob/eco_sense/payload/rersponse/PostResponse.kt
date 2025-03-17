package de.jakob.eco_sense.payload.rersponse

import de.jakob.eco_sense.model.Post
import de.jakob.eco_sense.model.User
import de.jakob.eco_sense.repository.CommentRepository
import de.jakob.eco_sense.repository.LikeRepository

data class PostResponse(
    val postId: String,
    val content: String,
    val imageUrl: String?,
    val createdAt: String,
    val userId: String,
    val username: String,
    val comments: Int,
    val likes: Int,
    val likedByUser: Boolean
) {
    companion object {
        fun fromEntity(
            post: Post,
            user: User?,
            likeRepository: LikeRepository,
            commentRepository: CommentRepository
        ): PostResponse {
            val likeCount = post.id?.let { likeRepository.countByPostId(it) } ?: 0
            val commentCount = post.id?.let { commentRepository.countByPostId(it) } ?: 0
            val userHasLiked = user?.let { likeRepository.findByUserIdAndPostId(userId = user.id!!, postId = post.id!!).isPresent }?: false
            return PostResponse(
                postId = post.id?: "No id assigned yet",
                username = post.user.username,
                userId = post.user.id!!,
                content = post.content,
                imageUrl = post.imageUrl,
                likes = likeCount,
                createdAt = post.createdAt.toString(),
                likedByUser = userHasLiked,
                comments = commentCount
            )
        }
    }
}