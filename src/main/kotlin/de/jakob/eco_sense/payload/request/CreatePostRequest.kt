package de.jakob.eco_sense.payload.request

data class CreatePostRequest(
    val content: String,
    val imageUrl: String?
)