package org.example.product.core.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import org.example.product.core.utils.SessionManager
import org.example.product.core.utils.SessionState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onHomeTabClicked()
    fun onProfileTabClicked()
    fun onLoginSuccess()
    fun onLogout()

    sealed class Child {
        class SplashChild(val component: SplashComponent) : Child()
        class LoginChild(val component: LoginComponent) : Child()
        class HomeChild(val component: HomeComponent) : Child()
        class ProfileChild(val component: ProfileComponent) : Child()
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext, KoinComponent {

    private val sessionManager: SessionManager by inject()
    private val navigation = StackNavigation<Config>()
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Splash,
            handleBackButton = true,
            childFactory = ::child,
        )

    init {
        sessionManager.sessionState
            .onEach { state ->
                if (state is SessionState.ForceLoggedOut) {
                    navigation.replaceAll(Config.Login)
                    sessionManager.logout() // Reset to LoggedOut
                }
            }
            .launchIn(scope)
    }

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Splash -> RootComponent.Child.SplashChild(
                DefaultSplashComponent(
                    componentContext = componentContext,
                    onFinished = { isValid ->
                        if (isValid) {
                            navigation.replaceAll(Config.Home)
                        } else {
                            navigation.replaceAll(Config.Login)
                        }
                    }
                )
            )
            is Config.Login -> RootComponent.Child.LoginChild(
                DefaultLoginComponent(
                    componentContext = componentContext,
                    onLoginSuccess = {
                        print("jembottttt login")
                        navigation.replaceAll(Config.Home)
                    }
                )
            )
            is Config.Home -> RootComponent.Child.HomeChild(
                DefaultHomeComponent(
                    componentContext = componentContext
                )
            )
            is Config.Profile -> RootComponent.Child.ProfileChild(
                DefaultProfileComponent(
                    componentContext = componentContext,
                    onLogout = {
                        println("jembot logout")
                        sessionManager.logout()
                        navigation.replaceAll(Config.Login)
                    }
                )
            )
        }

    override fun onHomeTabClicked() {
        navigation.replaceAll(Config.Home)
    }

    override fun onProfileTabClicked() {
        navigation.replaceAll(Config.Profile)
    }

    override fun onLoginSuccess() {
        navigation.replaceAll(Config.Home)
    }

    override fun onLogout() {
        sessionManager.logout()
        navigation.replaceAll(Config.Login)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Splash : Config
        @Serializable
        data object Login : Config
        @Serializable
        data object Home : Config
        @Serializable
        data object Profile : Config
    }
}

interface SplashComponent {
    fun onSplashFinished(isValid: Boolean)
}

class DefaultSplashComponent(
    componentContext: ComponentContext,
    private val onFinished: (Boolean) -> Unit
) : SplashComponent, ComponentContext by componentContext {
    override fun onSplashFinished(isValid: Boolean) {
        onFinished(isValid)
    }
}

interface LoginComponent {
    fun onLoginSuccess()
}

class DefaultLoginComponent(
    componentContext: ComponentContext,
    private val onLoginSuccess: () -> Unit
) : LoginComponent, ComponentContext by componentContext {
    override fun onLoginSuccess() {
        onLoginSuccess.invoke()
    }
}

interface HomeComponent {
}

class DefaultHomeComponent(
    componentContext: ComponentContext,
) : HomeComponent, ComponentContext by componentContext {
}

interface ProfileComponent {
    fun onLogout()
}

class DefaultProfileComponent(
    componentContext: ComponentContext,
    private val onLogout: () -> Unit
) : ProfileComponent, ComponentContext by componentContext {
    override fun onLogout() {
        onLogout.invoke()
    }
}
