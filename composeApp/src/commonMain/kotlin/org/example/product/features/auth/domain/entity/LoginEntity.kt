package org.example.product.features.auth.domain.entity

// features/auth/domain/model/AuthUser.kt
data class AuthUser(
    val id: Int,
    val fullName: String,
    val email: String,
    val token: String
)