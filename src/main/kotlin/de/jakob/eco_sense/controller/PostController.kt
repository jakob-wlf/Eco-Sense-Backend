package de.jakob.eco_sense.controller

import de.jakob.eco_sense.payload.request.CreatePostRequest
import de.jakob.eco_sense.payload.request.UpdatePostRequest
import de.jakob.eco_sense.payload.rersponse.PostResponse
import de.jakob.eco_sense.service.PostService
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {
    @GetMapping
    fun getAllPosts(
        @RequestHeader("Authorization", required = false) token: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<PostResponse>> {
        return ResponseEntity.ok(postService.getPaginatedPosts(page, size, token))
    }

    @GetMapping("/{id}")
    fun getPost(
        @RequestHeader("Authorization", required = false) token: String?,
        @PathVariable id: String
    ): ResponseEntity<PostResponse> {
        return ResponseEntity.ok(postService.getPost(id, token))
    }

    @PostMapping
    fun createPost(
        @RequestHeader("Authorization") token: String,
        @RequestBody createPostRequest: CreatePostRequest
    ): ResponseEntity<PostResponse> {
        return ResponseEntity.ok(postService.createPost(token, createPostRequest))
    }

    @PutMapping("/{id}")
    fun editPost(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String,
        @RequestBody updatePostRequest: UpdatePostRequest
    ): ResponseEntity<PostResponse> {
        return ResponseEntity.ok(postService.editPost(token, id, updatePostRequest))
    }

    @DeleteMapping("/{id}")
    fun deletePost(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String
    ): ResponseEntity<String> {
        postService.deletePost(token, id)
        return ResponseEntity.ok("Post deleted successfully")
    }

    @PostMapping("/{id}/like")
    fun likePost(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String
    ): ResponseEntity<PostResponse> {
        return ResponseEntity.ok(postService.likePost(token, id))
    }

    @DeleteMapping("/{id}/like")
    fun unlike(
        @RequestHeader("Authorization") token: String,
        @PathVariable id: String
    ): ResponseEntity<PostResponse> {
        return ResponseEntity.ok(postService.unlikePost(token, id))
    }
}