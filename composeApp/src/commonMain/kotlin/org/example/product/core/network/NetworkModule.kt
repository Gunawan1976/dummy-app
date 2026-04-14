package org.example.product.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
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

// ... import lainnya ...

val networkModule = module {
    single<TokenProvider> { TokenProviderImpl() }
    single { SessionManager(get()) }

    single {
        val tokenProvider = get<TokenProvider>()
        val sessionManager = get<SessionManager>()

        // HAPUS "val client = " DI SINI
        // Langsung return HttpClient
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
                        println("Ktor => $message")
                    }
                }
                level = LogLevel.ALL
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenProvider.getAccessToken()
                        val refreshToken = tokenProvider.getRefreshToken()
                        if (!accessToken.isNullOrBlank() && !refreshToken.isNullOrBlank()) {
                            BearerTokens(accessToken, refreshToken)
                        } else null
                    }

                    sendWithoutRequest { request ->
                        val path = request.url.buildString()
                        path.contains("auth/me") || path.contains("products")
                    }

                    refreshTokens {
                        val oldRefreshToken = tokenProvider.getRefreshToken() ?: return@refreshTokens null
                        try {
                            val refreshResponse = client.post("auth/refresh") {
                                contentType(ContentType.Application.Json)
                                setBody(RefreshRequest(refreshToken = oldRefreshToken))
                            }.body<RefreshTokenResponse>()

                            tokenProvider.saveTokens(
                                accessToken = refreshResponse.token,
                                refreshToken = refreshResponse.refreshToken
                            )

                            BearerTokens(
                                accessToken = refreshResponse.token,
                                refreshToken = refreshResponse.refreshToken
                            )
                        } catch (e: Exception) {
                            tokenProvider.clearTokens()
                            sessionManager.forceLogout() // Paksa user ke layar Login
                            null
                        }
                    }
                }
            }

            defaultRequest {
                url("https://dummyjson.com/")
            }
        } // Blok HttpClient langsung menjadi return value dari blok single{} Koin
    }
}