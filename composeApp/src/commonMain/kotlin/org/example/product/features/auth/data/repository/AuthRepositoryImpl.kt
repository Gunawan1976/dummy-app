package org.example.product.features.auth.data.repository

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import org.example.product.core.utils.Results
import org.example.product.features.auth.data.model.LoginRequest
import org.example.product.features.auth.data.sources.AuthApi
import org.example.product.features.auth.domain.entity.AuthUser
import org.example.product.features.auth.domain.repository.AuthRepository

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
                accessToken = response.accessToken ?: "",
                refreshToken = response.refreshToken ?: ""
            )

            Results.Success(authUser)
        } catch (e: ClientRequestException) {
            Results.Error("Login gagal: Username atau password salah.", e)
        } catch (e: ServerResponseException) {
            Results.Error("Terjadi kesalahan pada server. Coba lagi nanti.", e)
        } catch (e: IOException) {
            Results.Error("Tidak ada koneksi internet. Periksa jaringan Anda.", e)
        } catch (e: Exception) {
            Results.Error("Terjadi kesalahan yang tidak terduga: ${e.message}", e)
        }
    }

    override suspend fun getCurrentUser(): Results<AuthUser> {
        return try {
            val response = authApi.getCurrentUser()

            val authUser = AuthUser(
                id = response.id,
                fullName = "${response.firstName} ${response.lastName}",
                email = response.email,
                accessToken = response.accessToken ?: "",
                refreshToken = response.refreshToken ?: ""
            )

            Results.Success(authUser)
        } catch (e: ClientRequestException) {
            Results.Error("Gagal mengambil data user.", e)
        } catch (e: ServerResponseException) {
            Results.Error("Terjadi kesalahan pada server.", e)
        } catch (e: IOException) {
            Results.Error("Tidak ada koneksi internet.", e)
        } catch (e: Exception) {
            Results.Error("Terjadi kesalahan yang tidak terduga: ${e.message}", e)
        }
    }
}