package org.example.product.features.auth.data.sources

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import org.example.product.features.auth.data.model.AuthResponse
import org.example.product.features.auth.data.model.LoginRequest

// features/auth/data/remote/AuthApi.kt
class AuthApi(private val httpClient: HttpClient) {

    suspend fun login(request: LoginRequest): AuthResponse {
        return httpClient.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getCurrentUser(): AuthResponse {
        return httpClient.get("auth/me").body()
    }
}