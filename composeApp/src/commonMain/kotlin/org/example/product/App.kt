package org.example.product

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import org.example.product.core.navigation.RootComponent
import org.example.product.core.navigation.RootContent
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun App(root: RootComponent) {
    MaterialTheme {
        RootContent(component = root)
    }
}