package org.example.product.features.product.data.model

import kotlinx.serialization.*
import org.example.product.features.product.domain.entity.*

@Serializable
data class ProdukResponse (
    val products: List<ProductModel>,
    val total: Long,
    val skip: Long,
    val limit: Long
)

@Serializable
data class ProductModel (
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
    val dimensions: DimensionsModel,
    val warrantyInformation: String,
    val shippingInformation: String,
    val availabilityStatus: String,
    val reviews: List<ReviewModel>,
    val returnPolicy: String,
    val minimumOrderQuantity: Long,
    val meta: MetaModel,
    val images: List<String>,
    val thumbnail: String
) {
    fun toProductEntity() = ProductEntity(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        discountPercentage = discountPercentage,
        rating = rating,
        stock = stock,
        tags = tags,
        brand = brand,
        sku = sku,
        weight = weight,
        dimensions = dimensions.toDimensions(),
        warrantyInformation = warrantyInformation,
        shippingInformation = shippingInformation,
        availabilityStatus = availabilityStatus,
        reviews = reviews.map { it.toReview() },
        returnPolicy = returnPolicy,
        minimumOrderQuantity = minimumOrderQuantity,
        meta = meta.toMeta(),
        images = images,
        thumbnail = thumbnail
    )
}

@Serializable
data class DimensionsModel (
    val width: Double,
    val height: Double,
    val depth: Double
) {
    fun toDimensions() = Dimensions(width, height, depth)
}

@Serializable
data class MetaModel (
    val createdAt: String,
    val updatedAt: String,
    val barcode: String,
    val qrCode: String
) {
    fun toMeta() = Meta(createdAt, updatedAt, barcode, qrCode)
}

@Serializable
data class ReviewModel (
    val rating: Long,
    val comment: String,
    val date: String,
    val reviewerName: String,
    val reviewerEmail: String
) {
    fun toReview() = Review(rating, comment, date, reviewerName, reviewerEmail)
}
