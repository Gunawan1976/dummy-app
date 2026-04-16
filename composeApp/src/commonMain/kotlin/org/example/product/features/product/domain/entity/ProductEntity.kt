package org.example.product.features.product.domain.entity

data class ProdukResponse (
    val productEntities: List<ProductEntity>,
    val total: Long,
    val skip: Long,
    val limit: Long
)

data class ProductEntity (
    val id: Long,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Long,
    val tags: List<String>,
    val brand: String? = null,
    val sku: String,
    val weight: Long,
    val dimensions: Dimensions,
    val warrantyInformation: String,
    val shippingInformation: String,
    val availabilityStatus: String,
    val reviews: List<Review>,
    val returnPolicy: String,
    val minimumOrderQuantity: Long,
    val meta: Meta,
    val images: List<String>,
    val thumbnail: String
)

data class Dimensions (
    val width: Double,
    val height: Double,
    val depth: Double
)

data class Meta (
    val createdAt: String,
    val updatedAt: String,
    val barcode: String,
    val qrCode: String
)

data class Review (
    val rating: Long,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
)
