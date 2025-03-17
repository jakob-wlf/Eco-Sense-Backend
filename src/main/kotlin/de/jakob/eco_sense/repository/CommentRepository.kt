package de.jakob.eco_sense.repository

import de.jakob.eco_sense.model.Comment
import de.jakob.eco_sense.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, String> {
    fun findAllByPostId(postId: String): List<Comment>
    fun countByPostId(postId: String): Int
    fun findByPostId(postId: String, pageable: Pageable): Page<Comment>
}