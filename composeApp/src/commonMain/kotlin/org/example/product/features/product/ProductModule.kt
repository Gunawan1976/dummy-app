package org.example.product.features.product

import org.example.product.features.product.data.repository.ProductRepositoryImpl
import org.example.product.features.product.data.sources.ProductApi
import org.example.product.features.product.domain.repository.ProductRepository
import org.example.product.features.product.domain.usecase.GetProductUseCase
import org.example.product.features.product.presentation.viewmodel.ProductViewModel
import org.koin.dsl.module

val productModule = module {

    single { ProductApi(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }

    factory { GetProductUseCase(get()) }
    factory { ProductViewModel(get()) }

}