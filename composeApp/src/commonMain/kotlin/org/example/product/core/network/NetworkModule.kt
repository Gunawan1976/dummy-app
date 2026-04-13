package org.example.product.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.example.product.core.utils.SessionManager
import org.example.product.core.utils.TokenProvider
import org.example.product.core.utils.TokenProviderImpl
import org.example.product.features.auth.data.model.RefreshRequest
import org.example.product.features.auth.data.model.RefreshTokenResponse
import org.koin.dsl.module

// core/di/NetworkModule.kt
val networkModule = module {
    single<TokenProvider> { TokenProviderImpl() }

    single { SessionManager(get()) }

    single {
        val tokenProvider = get<TokenProvider>()
        val sessionManager = get<SessionManager>()

        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("Ktor => $message") // Print ke console/logcat
                    }
                }
                level = LogLevel.ALL
            }

            install(Auth) {
                bearer {
                    // Fungsi ini dipanggil setiap kali request dibuat
                    loadTokens {
                        val accessToken = tokenProvider.getAccessToken()
                        val refreshToken = tokenProvider.getRefreshToken()
                        if (accessToken != null && refreshToken != null) {
                            BearerTokens(accessToken, refreshToken)
                        } else null
                    }

                    refreshTokens {
                        // Ambil refresh token yang lama dari lokal
                        val oldRefreshToken = tokenProvider.getRefreshToken()
                            ?: return@refreshTokens null // Jika tidak ada, batalkan refresh

                        try {
                            // Panggil API Refresh DummyJSON (Gunakan parameter 'client' bawaan dari Ktor)
                            val refreshResponse = client.post("auth/refresh") {
                                contentType(ContentType.Application.Json)
                                setBody(RefreshRequest(refreshToken = oldRefreshToken))
                            }.body<RefreshTokenResponse>()

                            // Simpan token baru ke lokal
                            tokenProvider.saveTokens(
                                accessToken = refreshResponse.token,
                                refreshToken = refreshResponse.refreshToken
                            )

                            // Kembalikan token baru ke Ktor agar request yang gagal (401) bisa diulang
                            BearerTokens(
                                accessToken = refreshResponse.token,
                                refreshToken = refreshResponse.refreshToken
                            )
                        } catch (e: Exception) {
                            // Jika refresh token juga gagal/expired (misal: HTTP 400/401)
                            // Hapus token lokal agar user ter-logout dan kembali ke halaman Login
                            tokenProvider.clearTokens()
                            sessionManager.forceLogout()
                            null
                        }
                    }
                }
            }

            defaultRequest {
                url("https://dummyjson.com/")
            }
        }
    }
}