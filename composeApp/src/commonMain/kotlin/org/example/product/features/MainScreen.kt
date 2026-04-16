package org.example.product.features

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.example.product.core.utils.SessionState
import org.example.product.features.auth.presentation.ui.LoginScreen
import org.example.product.features.auth.presentation.ui.ProfileScreen
import org.example.product.features.auth.presentation.viewmodel.SessionViewModel
import org.example.product.features.product.presentation.ui.HomeProductScreen
import org.example.product.features.splash.presentation.SplashScreen
import org.koin.compose.viewmodel.koinViewModel

// 1. Buat Data Class untuk Item Bottom Nav
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home_route", "Home", Icons.Default.Home)
    object Profile : BottomNavItem("profile_route", "Profile", Icons.Default.Person)
}

@Composable
fun MainScreen(
    sessionViewModel: SessionViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val sessionState by sessionViewModel.sessionState.collectAsState()

    // Daftar menu bottom nav
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile
    )

    // Cek apakah bottom bar harus ditampilkan (Sembunyikan jika di layar login)
    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    // Hanya observasi untuk Force Logout. Navigasi masuk dikendalikan startDestination.
    LaunchedEffect(sessionState) {
        if (sessionState is SessionState.ForceLoggedOut) {
            navController.navigate("login_route") {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
            sessionViewModel.resetStateToLoggedOut()
        }
    }

    // 3. Gunakan Scaffold untuk meletakkan Bottom Bar
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier.height(120.dp),
                ) {
                    bottomNavItems.forEach { item ->
                        // Tentukan apakah item ini sedang aktif
                        val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // 4. NavHost diletakkan di dalam Scaffold padding
        NavHost(
            navController = navController,
            startDestination = "splash_route",
//            modifier = Modifier.padding(innerPadding)
        ) {

            composable("splash_route") {
                SplashScreen(
                    onNavigateToHome = {
                        navController.navigate(BottomNavItem.Home.route) {
                            // Hapus splash dari riwayat agar saat user tekan 'back', aplikasi keluar
                            popUpTo("splash_route") { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate("login_route") {
                            popUpTo("splash_route") { inclusive = true }
                        }
                    }
                )
            }

            composable("login_route") {
                LoginScreen(onLoginSuccess = {
                    // Ini yang akan dieksekusi saat LaunchedEffect di atas terpanggil!
                    navController.navigate(BottomNavItem.Home.route) {
                        // popUpTo penting agar user tidak bisa kembali ke halaman login pakai tombol 'back'
                        popUpTo("login_route") { inclusive = true }
                    }
                })
            }

            composable("home_route") {
                HomeProductScreen(

                )
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
}