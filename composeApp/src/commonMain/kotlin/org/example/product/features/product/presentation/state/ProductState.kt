package org.example.product.features.product.presentation.state

import org.example.product.features.auth.domain.entity.AuthUser
import org.example.product.features.auth.presentation.state.AuthState
import org.example.product.features.product.domain.entity.ProductEntity

sealed class ProductState {
    object Idle : ProductState()
    object Loading : ProductState()
    data class Success(val user: List<ProductEntity>) : ProductState()
    data class Error(val message: String) : ProductState()
}