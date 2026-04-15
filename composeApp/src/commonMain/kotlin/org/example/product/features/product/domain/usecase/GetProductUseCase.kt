package org.example.product.features.product.domain.usecase

import org.example.product.core.utils.Results
import org.example.product.features.product.domain.entity.ProductEntity
import org.example.product.features.product.domain.repository.ProductRepository

class GetProductUseCase(private val repository: ProductRepository){
    suspend operator fun invoke(): Results<List<ProductEntity>> {
        return repository.getProducts()

    }
}