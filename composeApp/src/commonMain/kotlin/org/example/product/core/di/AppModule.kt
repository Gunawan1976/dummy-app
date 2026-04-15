package org.example.product.core.di

import org.example.product.core.network.networkModule
import org.example.product.features.auth.authModule
import org.example.product.features.product.productModule
import org.example.product.viewModelModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

// shared/src/commonMain/kotlin/com/domain/app/di/AppModule.kt
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        networkModule,
        authModule,
        viewModelModule,
        productModule
        // tambahkan modul fitur lain di sini nantinya
    )
}

// Untuk dipanggil dari iOS (Helper)
fun initKoin() = initKoin {}