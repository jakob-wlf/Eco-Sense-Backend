package de.jakob.eco_sense.service

import de.jakob.eco_sense.exception.UnauthorizedException
import de.jakob.eco_sense.model.Credentials
import de.jakob.eco_sense.repository.CredentialsRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CredentialsService(
    private val credentialsRepository: CredentialsRepository
) {
    fun hashPassword(password: String): String {
        // Replace this with actual hashing logic, e.g., BCrypt
        return password
    }

    fun checkPassword(rawPassword: String, hashedPassword: String): Boolean {
        // Replace this with actual hash verification logic
        return rawPassword == hashedPassword
    }

    fun getCredentialsByUserId(userId: String): Credentials {
        return credentialsRepository.findByUserId(userId)
            .orElseThrow { UnauthorizedException("Invalid credentials") }
    }

    fun updatePassword(userId: String, newPassword: String) {
        val credentials = getCredentialsByUserId(userId)
        val updatedCredentials = credentials.copy(passwordHash = hashPassword(newPassword))
        credentialsRepository.save(updatedCredentials)
    }
}
