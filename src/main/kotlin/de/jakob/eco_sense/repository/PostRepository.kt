package de.jakob.eco_sense.repository

import de.jakob.eco_sense.model.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, String> {
    fun findAllByUserId(userId: String): List<Post>
    override fun findAll(pageable: Pageable): Page<Post>

}
