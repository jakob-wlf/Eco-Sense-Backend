package de.jakob.eco_sense.repository

import de.jakob.eco_sense.model.Credentials
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CredentialsRepository : JpaRepository<Credentials, String> {
    fun findByUserId(userId: String): Optional<Credentials>
}