package de.jakob.eco_sense.service

import de.jakob.eco_sense.exception.*
import de.jakob.eco_sense.model.Like
import de.jakob.eco_sense.model.Post
import de.jakob.eco_sense.payload.request.CreatePostRequest
import de.jakob.eco_sense.payload.request.UpdatePostRequest
import de.jakob.eco_sense.payload.rersponse.PostResponse
import de.jakob.eco_sense.repository.CommentRepository
import de.jakob.eco_sense.repository.LikeRepository
import de.jakob.eco_sense.repository.PostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class PostService(
    private val postRepository: PostRepository,
    private val likeRepository: LikeRepository,
    private val commentRepository: CommentRepository,
    private val userService: UserService
) {

    fun getPaginatedPosts(page: Int, size: Int, sessionToken: String?): Page<PostResponse> {
        val user = sessionToken?.let { userService.getUserEntityBySessionToken(sessionToken) }

        val sort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id"))
        val pageable = PageRequest.of(page, size, sort)

        return postRepository.findAll(pageable)
            .map { PostResponse.fromEntity(it, user, likeRepository, commentRepository) }
    }

    fun getPost(postId: String, sessionToken: String?): PostResponse {
        val user = sessionToken?.let { userService.getUserEntityBySessionToken(sessionToken) }

        val post = postRepository.findById(postId).orElseThrow {
            NotFoundException("Post not found")
        }

        return PostResponse.fromEntity(post, user, likeRepository, commentRepository)
    }

    fun getPostEntity(postId: String): Post {
        return postRepository.findById(postId).orElseThrow {
            NotFoundException("Post not found")
        }
    }

    fun createPost(token: String, createPostRequest: CreatePostRequest): PostResponse {
        val user = userService.getUserEntityBySessionToken(token)

        val content = createPostRequest.content
        if(content.length > 512) {
            throw PostTooLongException("Post content exceeds allowed length")
        }

        if(content.isEmpty()) {
            throw PostTooShortException("Post content must not be empty")
        }

        val post = Post(
            user = user,
            content = content,
            imageUrl = createPostRequest.imageUrl
        )

        postRepository.save(post)
        postRepository.flush()

        return PostResponse.fromEntity(post, user, likeRepository, commentRepository)
    }

    fun editPost(token: String, id: String, updatePostRequest: UpdatePostRequest): PostResponse? {
        val user = userService.getUserEntityBySessionToken(token)

        val content = updatePostRequest.content
        if(content != null) {
            if(content.length > 512) {
                throw PostTooLongException("Post content exceeds allowed length")
            }

            if(content.isEmpty()) {
                throw PostTooShortException("Post content must not be empty")
            }
        }

        val post = postRepository.findById(id).orElseThrow {NotFoundException("Post not found")}

        if(post.user != user) {
            throw UnauthorizedException("This post does not belong to you")
        }

        val updatedPost = post.copy(
            user = post.user,
            content = content?: post.content,
            imageUrl = updatePostRequest.imageUrl?: post.imageUrl
        )

        postRepository.save(updatedPost)
        postRepository.flush()

        return PostResponse.fromEntity(updatedPost, user, likeRepository, commentRepository)
    }

    fun deletePost(token: String, id: String) {
        val user = userService.getUserEntityBySessionToken(token)

        val post = postRepository.findById(id).orElseThrow {NotFoundException("Post not found")}

        if(post.user != user) {
            throw UnauthorizedException("This post does not belong to you")
        }

        postRepository.delete(post)
    }

    fun likePost(token: String, id: String): PostResponse {
        val user = userService.getUserEntityBySessionToken(token)

        val post = postRepository.findById(id).orElseThrow {NotFoundException("Post not found")}

        if(likeRepository.findByUserIdAndPostId(user.id!!, post.id!!).isPresent)
            throw ConflictException("You have already liked the post")

        val like = Like(
            user = user,
            post = post
        )

        likeRepository.save(like)
        likeRepository.flush()

        return PostResponse.fromEntity(post, user, likeRepository, commentRepository)
    }

    fun unlikePost(token: String, id: String): PostResponse? {
        val user = userService.getUserEntityBySessionToken(token)

        val post = postRepository.findById(id).orElseThrow {NotFoundException("Post not found")}

        val like = likeRepository.findByUserIdAndPostId(userId = user.id!!, postId = post.id!!).orElseThrow { NotFoundException("You have not liked this post") }

        likeRepository.delete(like)

        return PostResponse.fromEntity(post, user, likeRepository, commentRepository)
    }

}