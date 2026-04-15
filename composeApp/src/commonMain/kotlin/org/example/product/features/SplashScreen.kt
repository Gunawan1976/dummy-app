package org.example.product.features.splash.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.product.features.auth.presentation.state.AuthState
import org.example.product.features.auth.presentation.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    authViewModel: AuthViewModel = koinViewModel(), // Gunakan AuthViewModel
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val scale = remember { Animatable(0f) }

    // Amati status dari API
    val authState by authViewModel.authState.collectAsState()

    // Status untuk memastikan animasi minimal berjalan 1 detik
    var isAnimationFinished by remember { mutableStateOf(false) }

    // 1. JALANKAN ANIMASI & HIT API SECARA PARALEL SAAT SPLASH DIBUKA
    LaunchedEffect(Unit) {
        // Coroutine 1: Panggil API ke Server
        launch {
            authViewModel.checkSessionOnSplash()
        }

        // Coroutine 2: Jalankan Animasi UI
        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = { t ->
                        val u = t - 1
                        u * u * ((2.0f + 1) * u + 2.0f) + 1.0f
                    }
                )
            )
            // Tambahkan sedikit jeda agar logo terlihat jelas sebelum hilang
            delay(400)
            isAnimationFinished = true
        }
    }

    // 2. EVALUASI HASIL (Tunggu Animasi Selesai & API Selesai)
    LaunchedEffect(authState, isAnimationFinished) {
        // Jangan pindah layar kalau animasi belum selesai (agar tidak kedip)
        if (isAnimationFinished) {
            when (authState) {
                is AuthState.Success -> {
                    // Token Valid (atau sukses di-refresh), Lanjut ke Home!
                    onNavigateToHome()
                }
                is AuthState.Error -> {
                    // Token tidak ada, tidak valid, atau server mati -> Ke Login
                    onNavigateToLogin()
                }
                else -> {
                    // Jika masih Loading/Idle, diam saja tunggu API selesai membalas
                }
            }
        }
    }

    // Tampilan UI Splash Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.BrokenImage,
            contentDescription = "App Logo",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(120.dp)
                .scale(scale.value)
        )
    }
}