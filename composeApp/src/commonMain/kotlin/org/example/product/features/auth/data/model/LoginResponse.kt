package org.example.product.features.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val token: String

)