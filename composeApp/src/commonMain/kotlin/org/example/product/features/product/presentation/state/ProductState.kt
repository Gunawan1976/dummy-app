package org.example.product.features.product.presentation.state

import org.example.product.features.product.domain.entity.ProductEntity

sealed class ProductState {
    object Idle : ProductState()
    object Loading : ProductState()
    data class Success(val products: List<ProductEntity>) : ProductState()
    data class Error(val message: String) : ProductState()
}