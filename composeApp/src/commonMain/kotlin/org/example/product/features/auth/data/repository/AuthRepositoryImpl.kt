package org.example.product.features.auth.data.repository

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import org.example.product.core.utils.Results
import org.example.product.features.auth.data.model.LoginRequest
import org.example.product.features.auth.data.sources.AuthApi
import org.example.product.features.auth.domain.entity.AuthUser
import org.example.product.features.auth.domain.repository.AuthRepository

// features/auth/data/repository/AuthRepositoryImpl.kt
class AuthRepositoryImpl(
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun login(username: String, password: String): Results<AuthUser> {
        return try {
            val request = LoginRequest(username, password)
            val response = authApi.login(request)

            val authUser = AuthUser(
                id = response.id,
                fullName = "${response.firstName} ${response.lastName}",
                email = response.email,
                token = response.token
            )

            // Return Success
            Results.Success(authUser)

        } catch (e: ClientRequestException) {
            // HTTP 4xx Errors (misal: 400 Bad Request, 401 Unauthorized)
            // Dari DummyJSON biasanya karena password/username salah
            Results.Error("Login gagal: Username atau password salah.", e)

        } catch (e: ServerResponseException) {
            // HTTP 5xx Errors (Server DummyJSON sedang down)
            Results.Error("Terjadi kesalahan pada server. Coba lagi nanti.", e)

        } catch (e: IOException) {
            // Tidak ada koneksi internet / Timeout
            Results.Error("Tidak ada koneksi internet. Periksa jaringan Anda.", e)

        } catch (e: Exception) {
            // Error lainnya yang tidak terduga (misal error parsing JSON)
            Results.Error("Terjadi kesalahan yang tidak terduga: ${e.message}", e)
        }
    }
}