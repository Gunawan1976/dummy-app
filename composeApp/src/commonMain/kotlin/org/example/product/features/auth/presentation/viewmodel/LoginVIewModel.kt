package org.example.product.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.product.core.utils.Results
import org.example.product.core.utils.SessionManager
import org.example.product.core.utils.TokenProvider
import org.example.product.features.auth.domain.usecase.GetCurrentUserUseCase
import org.example.product.features.auth.domain.usecase.LoginUseCase
import org.example.product.features.auth.presentation.state.AuthState

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val tokenProvider: TokenProvider,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            when (val result = loginUseCase(username, password)) {
                is Results.Success -> {
                    // 1. Simpan Token
                    tokenProvider.saveTokens(
                        accessToken = result.data.accessToken,
                        refreshToken = result.data.refreshToken
                    )
                    // 2. Beri tahu aplikasi bahwa user sudah login
                    sessionManager.loginSuccess()

                    // 3. Update state UI
                    _authState.value = AuthState.Success(result.data)
                }
                is Results.Error -> {
                    _authState.value = AuthState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            when (val result = getCurrentUserUseCase()) {
                is Results.Success -> {
                    _authState.value = AuthState.Success(result.data)
                }
                is Results.Error -> {
                    // Biarkan Interceptor yang mengurus force logout jika 401.
                    // Di sini kita sekadar update state untuk pesan error umum (misal: No Internet)
                    _authState.value = AuthState.Error(result.message)
                }
                else -> {}
            }
        }
    }

    fun checkSessionOnSplash() {
        val token = tokenProvider.getAccessToken()

        if (token.isNullOrBlank()) {
            _authState.value = AuthState.Error("Token tidak ditemukan")
        } else {
            getCurrentUser()
        }
    }

    fun logout() {
        // Hapus token dan ubah status sesi global ke LoggedOut
        sessionManager.logout()

        _authState.value = AuthState.Idle
    }
}