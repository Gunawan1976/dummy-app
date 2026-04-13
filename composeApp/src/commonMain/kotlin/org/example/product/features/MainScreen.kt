package org.example.product.features

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.example.product.core.utils.SessionState
import org.example.product.features.auth.presentation.ui.LoginScreen
import org.example.product.features.auth.presentation.viewmodel.SessionViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    sessionViewModel: SessionViewModel = koinViewModel()
){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val sessionState by sessionViewModel.sessionState.collectAsState()

    // Dengarkan perubahan state yang bersifat one-time-event (seperti Force Logout)
    LaunchedEffect(sessionState) {
        if (sessionState is SessionState.ForceLoggedOut) {
            val message = (sessionState as SessionState.ForceLoggedOut).message

            // 1. Tampilkan pesan ke user (misal pakai Toast / Snackbar)
            println("PEMBERITAHUAN: $message")

            // 2. Arahkan navigasi paksa ke halaman Login, hapus semua backstack
            navController.navigate("login_route") {
                popUpTo(0) { inclusive = true }
            }

            // 3. Reset state agar tidak ter-trigger ulang jika layar diputar (recomposition)
            sessionViewModel.resetStateToLoggedOut()
        }
    }

    // Atur Root Navigation
    NavHost(
        navController = navController,
        startDestination = if (sessionState == SessionState.LoggedIn) "home_route" else "login_route"
    ) {
        composable("login_route") { LoginScreen() }
//        composable("home_route") { DashboardScreen() }
        // ... fitur lainnya ...
    }
}