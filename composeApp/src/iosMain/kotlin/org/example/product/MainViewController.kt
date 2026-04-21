package org.example.product

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import org.example.product.core.navigation.DefaultRootComponent

fun MainViewController() = ComposeUIViewController {
    val root = DefaultRootComponent(
        componentContext = DefaultComponentContext(LifecycleRegistry()),
    )
    App(root)
}
