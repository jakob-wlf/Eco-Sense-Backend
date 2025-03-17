package de.jakob.eco_sense.payload.request

data class LoginRequest(
    val email: String? = null,
    val username: String? = null,
    val password: String
)