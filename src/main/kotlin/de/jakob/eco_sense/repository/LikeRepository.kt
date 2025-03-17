package de.jakob.eco_sense.repository

import de.jakob.eco_sense.model.Like
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface LikeRepository : JpaRepository<Like, String> {
    fun findByUserIdAndPostId(userId: String, postId: String): Optional<Like>
    fun countByPostId(postId: String): Int
}