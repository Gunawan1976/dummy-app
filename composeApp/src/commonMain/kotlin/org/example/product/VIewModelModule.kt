package org.example.product

import org.example.product.features.auth.presentation.viewmodel.AuthViewModel
import org.example.product.features.auth.presentation.viewmodel.SessionViewModel
import org.koin.dsl.module

val viewModelModule = module {
    // Global/Session ViewModel (Singleton atau Factory tergantung kebutuhan)
    factory { SessionViewModel(get()) }

    // Feature ViewModels
    factory { AuthViewModel(get()) }
//    factory { DashboardViewModel(get()) }
//    factory { PantryViewModel(get()) } // Contoh fitur Pantry Manager-mu
}