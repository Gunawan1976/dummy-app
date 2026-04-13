package org.example.product.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.product.core.utils.Results
import org.example.product.features.auth.domain.usecase.LoginUseCase
import org.example.product.features.auth.presentation.state.AuthState

// features/auth/presentation/AuthViewModel.kt
// features/auth/presentation/AuthViewModel.kt
class AuthViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            // Panggil UseCase dan gunakan 'when' untuk mengecek Result
            when (val result = loginUseCase(username, password)) {
                is Results.Success -> {
                    // Data berhasil didapat
                    val user = result.data
                    _authState.value = AuthState.Success(user)

                    // TODO: Simpan user.token ke lokal (misal dengan Settings)
                }

                is Results.Error -> {
                    // Data gagal didapat, tampilkan pesan error yang sudah dirapikan di Repository
                    _authState.value = AuthState.Error(result.message)
                }

                else -> {}
            }
        }
    }
}