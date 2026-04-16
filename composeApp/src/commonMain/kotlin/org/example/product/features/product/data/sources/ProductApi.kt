package org.example.product.features.product.data.sources

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.example.product.features.product.data.model.ProdukResponse

class ProductApi(private val httpClient: HttpClient){

    suspend fun getProducts(): ProdukResponse {
        return httpClient.get("products").body()
    }
}