package de.jakob.eco_sense.repository

import de.jakob.eco_sense.model.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SessionRepository : JpaRepository<Session, String> {
    fun findBySessionToken(sessionToken: String): Optional<Session>
    fun deleteBySessionToken(sessionToken: String)
}