package org.example.product.features.auth.domain.entity

data class AuthUser(
    val id: Int,
    val fullName: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String
)