package de.jakob.eco_sense.service

import de.jakob.eco_sense.exception.NotFoundException
import de.jakob.eco_sense.exception.UnauthorizedException
import de.jakob.eco_sense.model.Credentials
import de.jakob.eco_sense.model.Session
import de.jakob.eco_sense.model.User
import de.jakob.eco_sense.payload.request.LoginRequest
import de.jakob.eco_sense.payload.request.RegisterRequest
import de.jakob.eco_sense.payload.request.UserUpdateRequest
import de.jakob.eco_sense.payload.rersponse.SessionResponse
import de.jakob.eco_sense.payload.rersponse.UserResponse
import de.jakob.eco_sense.repository.CredentialsRepository
import de.jakob.eco_sense.repository.SessionRepository
import de.jakob.eco_sense.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.sql.Timestamp
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val credentialsService: CredentialsService,
    private val validationService: ValidationService
    ) {

    @Transactional
    fun registerUser(request: RegisterRequest): SessionResponse {
        // Check if email is already in use
        if (userRepository.findByEmail(request.email).isPresent) {
            throw IllegalArgumentException("Email already in use")
        }
        // Check if username is already in use (fixed from the original mistake)
        if (userRepository.findByUsername(request.username).isPresent) {
            throw IllegalArgumentException("Username already in use")
        }

        if(!validationService.isUsernameValid(request.username)) {
            throw IllegalArgumentException("Username does not meet the requirements")
        }

        // Create a new User entity.
        val user = User(
            username = request.username,
            email = request.email
            // Optionally set other fields like profilePictureUrl or bio if needed
        )

        // Hash the password and create the Credentials entity.
        val passwordHash = credentialsService.hashPassword(request.password)
        val credentials = Credentials(
            user = user,
            passwordHash = passwordHash
        )
        user.credentials = credentials

        // Persist the new user. Cascading will save the credentials as well.
        val savedUser = userRepository.save(user)

        // Create a new session token. Here we use a UUID; you could also implement a more sophisticated token generator.
        val sessionToken = UUID.randomUUID().toString()

        // For example purposes, set the session to expire in 24 hours.
        val expiresAt = Timestamp(System.currentTimeMillis() + 24 * 60 * 60 * 1000)

        val session = Session(
            user = savedUser,
            sessionToken = sessionToken,
            expiresAt = expiresAt
        )

        // Persist the session.
        sessionRepository.save(session)

        // Return a session response.
        return SessionResponse(
            sessionToken = sessionToken,
        )
    }

    fun loginUser(request: LoginRequest): SessionResponse {
        // Ensure either email or username is provided.
        if (request.email.isNullOrBlank() && request.username.isNullOrBlank()) {
            throw IllegalArgumentException("Either email or username must be provided")
        }

        // Lookup user based on the provided identifier.
        val user = when {
            !request.email.isNullOrBlank() ->
                userRepository.findByEmail(request.email)
            !request.username.isNullOrBlank() ->
                userRepository.findByUsername(request.username)
            else -> throw IllegalArgumentException("Either email or username must be provided")
        }.orElseThrow { UnauthorizedException("Invalid credentials") }

        val credentials = credentialsService.getCredentialsByUserId(user.id!!)

        if (!credentialsService.checkPassword(request.password, credentials.passwordHash)) {
            throw UnauthorizedException("Invalid credentials")
        }

        val session = Session(
            user = user,
            sessionToken = generateSessionToken(),
            expiresAt = generateSessionExpiry()
        )
        sessionRepository.save(session)

        return SessionResponse(sessionToken = session.sessionToken)
    }

    fun getUserBySessionToken(token: String): UserResponse {
        val session = sessionRepository.findBySessionToken(token)
            .orElseThrow { UnauthorizedException("Invalid session") }

        return UserResponse.fromEntity(session.user)
    }

    fun getUserEntityById(id: String): User {
        return userRepository.findById(id)
            .orElseThrow { NotFoundException("User not found") }
    }

    fun getUserEntityBySessionToken(token: String): User {
        val session = sessionRepository.findBySessionToken(token)
            .orElseThrow { UnauthorizedException("Invalid session") }

        return session.user
    }

    fun getUserProfile(userId: String): UserResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { NotFoundException("User not found") }

        return UserResponse.fromEntity(user)
    }

    @Transactional
    fun deleteUser(token: String) {
        val session = sessionRepository.findBySessionToken(token)
            .orElseThrow { UnauthorizedException("Invalid session") }

        val user = session.user
        sessionRepository.deleteBySessionToken(token)
        userRepository.delete(user)
    }

    fun updateUser(token: String, updateRequest: UserUpdateRequest): UserResponse {
        val session = sessionRepository.findBySessionToken(token)
            .orElseThrow { UnauthorizedException("Invalid session") }

        val currentUser = session.user
        val newUsername = updateRequest.username ?: currentUser.username

        if(newUsername != currentUser.username) {
            if (!validationService.isUsernameValid(newUsername)) {
                throw IllegalArgumentException("Username does not meet the requirements")
            }
            if(userRepository.findByUsername(newUsername).isPresent) {
                throw IllegalArgumentException("Username already in use")
            }
        }

        val user = session.user.copy(
            username = updateRequest.username ?: session.user.username,
            bio = updateRequest.bio ?: session.user.bio,
            profilePictureUrl = updateRequest.profilePictureUrl ?: session.user.profilePictureUrl
        )

        val updatedUser = userRepository.save(user)
        return UserResponse.fromEntity(updatedUser)
    }

    private fun generateSessionToken(): String {
        return UUID.randomUUID().toString()
    }

    private fun generateSessionExpiry(): Timestamp {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 7) // Session expires in 7 days
        return Timestamp(calendar.timeInMillis)
    }

    fun logoutUser(token: String) {
        val session = sessionRepository.findBySessionToken(token)
            .orElseThrow { UnauthorizedException("Invalid session") }

        sessionRepository.delete(session)
    }

    fun getAllUsers(page: Int, size: Int): Page<UserResponse> {
        val sort = Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id"))
        val pageable = PageRequest.of(page, size, sort)

        return userRepository.findAll(pageable)
            .map { UserResponse.fromEntity(it) }
    }
}