package org.example.product.features.auth.domain.repository

import org.example.product.core.utils.Results
import org.example.product.features.auth.domain.entity.AuthUser

// features/auth/domain/repository/AuthRepository.kt
interface AuthRepository {
    suspend fun login(username: String, password: String): Results<AuthUser>

    suspend fun getCurrentUser(): Results<AuthUser>

}