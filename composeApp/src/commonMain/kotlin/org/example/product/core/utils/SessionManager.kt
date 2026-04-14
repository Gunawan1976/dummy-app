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

// core/utils/SessionManager.kt

class SessionManager(private val tokenProvider: TokenProvider) {

    private val _sessionState = MutableStateFlow<SessionState>(SessionState.Idle)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    // 1. Tambahkan callback pembersih cache
    var clearKtorCache: (() -> Unit)? = null

    init {
        if (tokenProvider.getAccessToken() != null) {
            _sessionState.value = SessionState.LoggedIn
        } else {
            _sessionState.value = SessionState.LoggedOut
        }
    }

    fun loginSuccess() {
        // 2. Panggil di sini agar Ktor membaca token yang baru didapat
        clearKtorCache?.invoke()
        _sessionState.value = SessionState.LoggedIn
    }

    fun logout() {
        tokenProvider.clearTokens()
        // 3. Panggil di sini agar Ktor membuang token yang lama
        clearKtorCache?.invoke()
        _sessionState.value = SessionState.LoggedOut
    }

    fun forceLogout(reason: String = "Sesi telah berakhir.") {
        tokenProvider.clearTokens()
        clearKtorCache?.invoke()
        _sessionState.value = SessionState.ForceLoggedOut(reason)
    }
}