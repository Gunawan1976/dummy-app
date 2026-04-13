package org.example.product.features.auth.domain.usecase

import org.example.product.core.utils.Results
import org.example.product.features.auth.domain.entity.AuthUser
import org.example.product.features.auth.domain.repository.AuthRepository

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Results<AuthUser> {
        return repository.getCurrentUser()
    }
}