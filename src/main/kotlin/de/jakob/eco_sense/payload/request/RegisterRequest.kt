package de.jakob.eco_sense.payload.request

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)