package org.example.product.features.product.data.sources

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.example.product.features.product.data.model.ProductModel

class ProductApi(private val httpClient: HttpClient){

    suspend fun getProducts(): List<ProductModel> {
        return httpClient.get("products").body()
    }
}