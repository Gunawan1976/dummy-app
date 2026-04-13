package org.example.product.features.auth.presentation.ui
import androidx.compose.ui.text.input.VisualTransformation // WAJIB untuk if-else visualTransformation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.example.product.features.auth.domain.usecase.LoginUseCase
import org.example.product.features.auth.presentation.viewmodel.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = koinViewModel(),
    onLoginSuccess : () -> Unit = {}
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(modifier = Modifier.width(IntrinsicSize.Min)) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("User") },
                    modifier = Modifier.fillMaxWidth() // Pastikan TextField juga fillMaxWidth
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {Text("Password" )},
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                    trailingIcon = {
//                        val image = if (passwordVisible)
//                            Icons.Filled.ThumbUp
//                        else Icons.Filled.Lock
//
//                        // Deskripsi untuk aksesibilitas
//                        val description = if (passwordVisible) "Hide password" else "Show password"
//
//                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                            Icon(imageVector = image, contentDescription = description)
//                        }
//                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(), // Lebar penuh mengikuti parent
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6750A4) // Warna ungu seperti di gambar
                    )
                ) {
                    Text(
                        "Login",
                        modifier = Modifier.padding(vertical = 8.dp) // Beri padding dalam agar tombol lebih tebal
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        LoginScreen(
            viewModel = AuthViewModel(
                loginUseCase = LoginUseCase(
                    repository = TODO()
                )
            ),
            onLoginSuccess = {}
        )
    }
}
