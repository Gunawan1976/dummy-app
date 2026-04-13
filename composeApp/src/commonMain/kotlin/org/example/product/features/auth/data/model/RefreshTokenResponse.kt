package org.example.product.features.auth.data.model

import kotlinx.serialization.Serializable

// features/auth/data/remote/dto/AuthResponse.kt
@Serializable
data class RefreshTokenResponse(
    val id: Int,
    val username: String,
    val email: String,
    val token: String, // Access Token dari DummyJSON
    val refreshToken: String // Tambahkan ini
)

// features/auth/domain/model/AuthUser.kt
data class AuthUser(
    val id: Int,
    val fullName: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)