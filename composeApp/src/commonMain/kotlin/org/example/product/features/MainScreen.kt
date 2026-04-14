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
import org.example.product.features.auth.presentation.ui.ProfileScreen
import org.example.product.features.auth.presentation.viewmodel.SessionViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    sessionViewModel: SessionViewModel = koinViewModel()
){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val sessionState by sessionViewModel.sessionState.collectAsState()

    LaunchedEffect(sessionState) {
        when (sessionState) {
            is SessionState.LoggedIn -> {
                // Jika sudah login, pastikan berada di Profile
                navController.navigate("profile_route") {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            is SessionState.LoggedOut, is SessionState.ForceLoggedOut -> {
                // Jika tidak ada token / logout, balik ke Login
                navController.navigate("login_route") {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            else -> {}
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (sessionState is SessionState.LoggedIn) "profile_route" else "login_route"
    ) {
        composable("login_route") { 
            LoginScreen(onLoginSuccess = {
                navController.navigate("profile_route") {
//                    popUpTo("login_route") { inclusive = true }
                }
            })
        }
        composable("profile_route") { 
            ProfileScreen(
                onLogoutClick = {
                    sessionViewModel.resetStateToLoggedOut()
                }
            )
        }
    }
}