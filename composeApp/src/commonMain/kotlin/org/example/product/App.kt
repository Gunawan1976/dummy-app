package org.example.product

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.example.product.features.MainScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainScreen()
    }
}