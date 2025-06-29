package de.jakob.eco_sense.repository

import de.jakob.eco_sense.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun findByUsername(username: String): Optional<User>
    fun findByEmail(email: String): Optional<User>
}