package org.example.product.features.auth.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.product.core.utils.components.shimmerEffect
import org.example.product.features.auth.presentation.state.AuthState
import org.example.product.features.auth.presentation.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AuthViewModel = koinViewModel(),
    onLogoutClick: () -> Unit // Tambahkan parameter callback ini
) {
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profil Pengguna") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (val state = authState) {
                is AuthState.Loading -> {
                    // MENGGUNAKAN SHIMMER EFFECT
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Shimmer Avatar
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .shimmerEffect()
                        )

                        // Shimmer Nama
                        Box(
                            modifier = Modifier
                                .height(32.dp)
                                .fillMaxWidth(0.6f)
                                .shimmerEffect()
                        )

                        // Shimmer Email
                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .fillMaxWidth(0.4f)
                                .shimmerEffect()
                        )
                    }
                }

                is AuthState.Success -> {
                    val user = state.user
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = user.fullName,
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onLogoutClick, // Panggil callback di sini
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Logout")
                        }
                    }
                }

                is AuthState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Error: ${state.message}",
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { viewModel.getCurrentUser() }) {
                            Text("Coba Lagi")
                        }
                    }
                }

                else -> {
                    Text("Memuat data...")
                }
            }
        }
    }
}