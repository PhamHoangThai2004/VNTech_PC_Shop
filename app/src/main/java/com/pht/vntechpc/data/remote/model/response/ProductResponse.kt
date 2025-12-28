package com.pht.vntechpc.data.remote.model.response

import com.pht.vntechpc.domain.model.Image
import com.pht.vntechpc.domain.model.Product
import com.pht.vntechpc.domain.model.Specification

data class ProductResponse(
    val id: Int,
    val productName: String,
    val description: String?,
    val originalPrice: Long,
    val salePrice: Long,
    val stock: Int,
    val quantitySold: Int,
    val brand: String,
    val model: String,
    val rating: Float,
    val origin: String?,
    val createdAt: String,
    val updatedAt: String,
    val category: CategoryResponse,
    val images: List<ImageResponse>?,
    val specifications: List<SpecificationResponse>?
)

fun ProductResponse.toProduct(): Product {
    return Product(
        id,
        productName,
        description,
        originalPrice,
        salePrice,
        stock,
        quantitySold,
        brand,
        model,
        rating,
        origin,
        createdAt,
        updatedAt,
        category.toCategory(),
        images?.map { it.toImage() },
        specifications?.map { it.toSpecification() }
    )
}

data class ImageResponse(
    val imageUrl: String,
    val main: Boolean
)

fun ImageResponse.toImage(): Image {
    return Image(imageUrl, main)
}

data class SpecificationResponse(
    val keyName: String,
    val value: String
)

fun SpecificationResponse.toSpecification(): Specification {
    return Specification(keyName, value)
}