package org.example.product.features.auth

import org.example.product.features.auth.data.repository.AuthRepositoryImpl
import org.example.product.features.auth.data.sources.AuthApi
import org.example.product.features.auth.domain.repository.AuthRepository
import org.example.product.features.auth.domain.usecase.GetCurrentUserUseCase
import org.example.product.features.auth.domain.usecase.LoginUseCase
import org.example.product.features.auth.presentation.viewmodel.AuthViewModel
import org.example.product.features.auth.presentation.viewmodel.SessionViewModel
import org.koin.dsl.module

// features/auth/di/AuthModule.kt
val authModule = module {
    // 1. Data Layer: API & Repository
    single { AuthApi(get()) } 
    single<AuthRepository> { AuthRepositoryImpl(get()) }

    // 2. Domain Layer: UseCase
    factory { LoginUseCase(get()) }
    factory { GetCurrentUserUseCase(get()) }

    // 3. Presentation Layer: ViewModel
    factory { AuthViewModel(get(), get(), get(), get()) }
    
    single { SessionViewModel(get()) }
}