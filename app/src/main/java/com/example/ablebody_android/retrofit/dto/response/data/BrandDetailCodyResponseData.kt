package com.example.ablebody_android.retrofit.dto.response.data

data class BrandDetailCodyResponseData(
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
            val imageURL: String,
            val createDate: String, // LocalDate 형태로 파싱하려면 LocalDate로 변경하고 파싱 라이브러리를 사용하세요.
            val comments: Int,
            val likes: Int,
            val views: Int,
            val plural: Boolean
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

}
