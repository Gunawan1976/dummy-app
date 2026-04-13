package org.example.product.features.auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import org.example.product.core.utils.SessionManager

// shared/src/commonMain/kotlin/com/domain/app/core/presentation/SessionViewModel.kt

class SessionViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    // Expose state ke UI
    val sessionState = sessionManager.sessionState

    fun resetStateToLoggedOut() {
        // Dipanggil setelah UI sukses menampilkan dialog error / navigasi selesai
        // agar state tidak nyangkut di "ForceLoggedOut" terus menerus
        sessionManager.logout()
    }
}