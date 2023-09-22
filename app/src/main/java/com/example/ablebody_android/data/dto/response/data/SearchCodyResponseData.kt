package com.example.ablebody_android.data.dto.response.data

import com.google.gson.annotations.SerializedName

data class SearchCodyResponseData (
    val content: List<Content>,
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
) {
    data class Content(
        val id: Long,
        val imageURL: String,
        val createDate: String,
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
        @SerializedName("unpaged")  val unPaged: Boolean
    )

    data class Sort(
        val empty: Boolean,
        val sorted: Boolean,
        val unsorted: Boolean
    )
}