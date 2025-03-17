package de.jakob.eco_sense.repository

import de.jakob.eco_sense.model.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NewsRepository : JpaRepository<News, String> {}