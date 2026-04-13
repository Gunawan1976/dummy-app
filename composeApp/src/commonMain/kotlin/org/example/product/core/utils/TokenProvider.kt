package org.example.product.core.utils

// core/utils/TokenProvider.kt
interface TokenProvider {
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    fun saveTokens(accessToken: String, refreshToken: String)
    fun clearTokens()
}

class TokenProviderImpl : TokenProvider {
    // Di aplikasi asli, ambil dari MultiplatformSettings atau EncryptedSharedPreferences
    override fun getAccessToken(): String? {
        TODO("Not yet implemented")
    }

    override fun getRefreshToken(): String? {
        TODO("Not yet implemented")
    }

    override fun saveTokens(accessToken: String, refreshToken: String) {
        TODO("Not yet implemented")
    }

    override fun clearTokens() {
        TODO("Not yet implemented")
    }
}