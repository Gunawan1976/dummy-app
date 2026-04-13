package org.example.product.features.auth.domain.usecase

import org.example.product.core.utils.Results
import org.example.product.features.auth.domain.entity.AuthUser
import org.example.product.features.auth.domain.repository.AuthRepository

// features/auth/domain/usecase/LoginUseCase.kt
class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Results<AuthUser> {

        // Validasi Bisnis di UseCase
        if (username.isBlank()) {
            return Results.Error("Username tidak boleh kosong")
        }
        if (password.length < 4) {
            return Results.Error("Password minimal 4 karakter")
        }

        return repository.login(username, password)
    }
}