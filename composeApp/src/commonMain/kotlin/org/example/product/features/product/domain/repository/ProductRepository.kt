package org.example.product.features.product.domain.repository

import org.example.product.core.utils.Results
import org.example.product.features.product.domain.entity.ProductEntity

interface ProductRepository{

    suspend fun getProducts(): Results<List<ProductEntity>>

}