package org.example.product.features.auth.data.model

import kotlinx.serialization.Serializable

// features/auth/data/remote/dto/RefreshRequest.kt
@Serializable
data class RefreshRequest(
    val refreshToken: String,
    val expiresInMins: Int = 30
)