package de.jakob.eco_sense.payload.request

data class UserUpdateRequest(
    val username: String?,
    val bio: String?,
    val profilePictureUrl: String?
)
