package org.example.product.core.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// shared/src/commonMain/kotlin/com/domain/app/core/utils/SessionManager.kt

sealed class SessionState {
    object Idle : SessionState()
    object LoggedIn : SessionState()
    object LoggedOut : SessionState()
    data class ForceLoggedOut(val message: String) : SessionState()
}

class SessionManager(private val tokenProvider: TokenProvider) {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Idle)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    init {
        // Cek status saat aplikasi pertama kali dibuka
        if (tokenProvider.getAccessToken() != null) {
            _sessionState.value = SessionState.LoggedIn
        } else {
            _sessionState.value = SessionState.LoggedOut
        }
    }

    fun loginSuccess() {
        _sessionState.value = SessionState.LoggedIn
    }

    fun logout() {
        tokenProvider.clearTokens()
        _sessionState.value = SessionState.LoggedOut
    }

    fun forceLogout(reason: String = "Sesi telah berakhir. Silakan login kembali.") {
        tokenProvider.clearTokens()
        _sessionState.value = SessionState.ForceLoggedOut(reason)
    }
}