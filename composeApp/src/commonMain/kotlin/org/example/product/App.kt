package org.example.product
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.product.core.utils.SessionState
import org.example.product.features.auth.presentation.ui.LoginScreen
import org.example.product.features.auth.presentation.viewmodel.AuthViewModel
import org.example.product.features.auth.presentation.viewmodel.SessionViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

import productexampleapp.composeapp.generated.resources.Res
import productexampleapp.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    // 1. Ambil Global ViewModel di level paling atas
    val sessionViewModel: SessionViewModel = koinViewModel()
    val sessionState by sessionViewModel.sessionState.collectAsState()

    val navController = rememberNavController()

    // 2. Observer Global untuk Force Logout
    LaunchedEffect(sessionState) {
        if (sessionState is SessionState.ForceLoggedOut) {
            // Navigasi paksa ke Login
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
            }
            sessionViewModel.resetStateToLoggedOut()
        }
    }

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = if (sessionState is SessionState.LoggedIn) "dashboard" else "login"
        ) {
            // 3. Masukkan ViewModel Spesifik per Fitur
            composable("login") {
                val authVm: AuthViewModel = koinViewModel()
                LoginScreen(viewModel = authVm, onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                })
            }

//            composable("dashboard") {
//                val dashboardVm: DashboardViewModel = koinViewModel()
//                DashboardScreen(viewModel = dashboardVm)
//            }
//
//            composable("pantry") {
//                // ViewModel fitur pantry hanya akan dibuat saat screen ini dibuka
//                val pantryVm: PantryViewModel = koinViewModel()
//                PantryScreen(viewModel = pantryVm)
//            }
        }
    }
}