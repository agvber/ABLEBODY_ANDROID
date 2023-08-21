package com.example.ablebody_android.retrofit.dto.response.data

data class BrandDetaiItemResponseData(
    val data: Page
){
    data class Page(
        val content: List<Item>,
        val pageable: Pageable,
        val totalPages: Int,
        val totalElements: Int,
        val last: Boolean,
        val number: Int,
        val sort: Sort,
        val size: Int,
        val numberOfElements: Int,
        val first: Boolean,
        val empty: Boolean
    ){
        data class Item(
            val id: Long,
            val name: String,
            val price: Int,
            val salePrice: Int?,
            val brandName: String,
            val image: String,
            val isPlural: Boolean,
            val url: String,
            val avgStarRating: String?
        )
        data class Pageable(
            val sort: Sort,
            val offset: Int,
            val pageNumber: Int,
            val pageSize: Int,
            val paged: Boolean,
            val unpaged: Boolean
        )
        data class Sort(
            val empty: Boolean,
            val sorted: Boolean,
            val unsorted: Boolean
        )
    }
    data class Sort(
        val empty: Boolean,
        val sorted: Boolean,
        val unsorted: Boolean
    )
}
