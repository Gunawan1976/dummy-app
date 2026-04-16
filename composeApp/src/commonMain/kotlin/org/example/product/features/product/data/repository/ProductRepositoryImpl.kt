package org.example.product.features.product.data.repository

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import org.example.product.core.utils.Results
import org.example.product.features.product.data.sources.ProductApi
import org.example.product.features.product.domain.entity.ProductEntity
import org.example.product.features.product.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val api: ProductApi
): ProductRepository{
    override suspend fun getProducts(): Results<List<ProductEntity>> {
        return try {
            val response = api.getProducts()
            Results.Success(
                data = response.products.map { it.toProductEntity() }
            )
        } catch (e: ClientRequestException) {
            Results.Error("Gagal mengambil data produk.", e)
        } catch (e: ServerResponseException) {
            Results.Error("Terjadi kesalahan pada server.", e)
        } catch (e: IOException) {
            Results.Error("Tidak ada koneksi internet.", e)
        } catch (e: Exception) {
            Results.Error("Terjadi kesalahan yang tidak terduga: ${e.message}", e)
        }
    }
}