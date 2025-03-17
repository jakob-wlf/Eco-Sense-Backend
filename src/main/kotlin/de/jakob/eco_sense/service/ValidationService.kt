package de.jakob.eco_sense.service

import de.jakob.eco_sense.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class ValidationService(
    private val userRepository: UserRepository
) {
    fun isUsernameValid(username: String): Boolean {
        // Add your username validation rules here (e.g., length, allowed characters, etc.)
        // Example: only letters, digits, underscores, between 3 and 20 characters.
        val regex = "^[a-zA-Z0-9_]{3,20}$".toRegex()
        return username.matches(regex)
    }
}
