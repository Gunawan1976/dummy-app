package org.example.product.core.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.example.product.features.auth.presentation.ui.LoginScreen
import org.example.product.features.auth.presentation.ui.ProfileScreen
import org.example.product.features.product.presentation.ui.HomeProductScreen
import org.example.product.features.splash.presentation.SplashScreen

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    val childStack by component.stack.subscribeAsState()
    val activeComponent = childStack.active.instance

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (activeComponent is RootComponent.Child.HomeChild || activeComponent is RootComponent.Child.ProfileChild) {
                BottomBar(
                    onHomeTabClicked = component::onHomeTabClicked,
                    onProfileTabClicked = component::onProfileTabClicked,
                    currentChild = activeComponent
                )
            }
        }
    ) { innerPadding ->
        Children(
            stack = childStack,
            modifier = Modifier.padding(innerPadding),
            animation = stackAnimation()
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.SplashChild -> SplashScreen(
                    onNavigateToHome = { child.component.onSplashFinished(true) },
                    onNavigateToLogin = { child.component.onSplashFinished(false) }
                )
                is RootComponent.Child.LoginChild -> LoginScreen(
                    onLoginSuccess = { child.component.onLoginSuccess() }
                )
                is RootComponent.Child.HomeChild -> HomeProductScreen()
                is RootComponent.Child.ProfileChild -> ProfileScreen(
                    onLogoutClick = { child.component.onLogout() }
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    onHomeTabClicked: () -> Unit,
    onProfileTabClicked: () -> Unit,
    currentChild: RootComponent.Child
) {
    NavigationBar(
        modifier = Modifier.height(120.dp),
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentChild is RootComponent.Child.HomeChild,
            onClick = onHomeTabClicked
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentChild is RootComponent.Child.ProfileChild,
            onClick = onProfileTabClicked
        )
    }
}
