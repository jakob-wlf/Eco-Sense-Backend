package de.jakob.eco_sense.repository

import de.jakob.eco_sense.model.CarbonFootprint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CarbonFootprintRepository : JpaRepository<CarbonFootprint, String> {
    fun findByUserId(userId: String): Optional<CarbonFootprint>
}