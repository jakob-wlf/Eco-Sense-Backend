package de.jakob.eco_sense.payload.rersponse

import de.jakob.eco_sense.model.User

data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    val profilePictureUrl: String?,
    val bio: String?,
    val createdAt: String
) {
    companion object {
        fun fromEntity(
            user: User
        ): UserResponse {
            return UserResponse(
                id = user.id!!,
                username = user.username,
                email = user.email,
                profilePictureUrl = user.profilePictureUrl,
                bio = user.bio,
                createdAt = user.createdAt.toString()
            )
        }
    }
}
