package com.example.ablebody_android.model


data class ProductItemData(
    val content: List<Item>,
    val totalPages: Int,
    val totalElements: Int,
    val last: Boolean,
    val number: Int,
    val size: Int,
    val numberOfElements: Int,
    val first: Boolean,
    val empty: Boolean
) {
    data class Item(
        val id: Long,
        val name: String,
        val price: Int,
        val salePrice: Int?,
        val brandName: String,
        val imageURL: String,
        val isSingleImage: Boolean,
        val url: String,
        val avgStarRating: String?
    )
}
