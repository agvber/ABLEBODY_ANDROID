package com.smilehunter.ablebody.model

data class UserBoardData(
    val content: List<Content>,
    val totalPages: Int,
    val last: Boolean,
    val number: Int,
    val first: Boolean,
) {
    data class Content(
        val id: Long,
        val imageURL: String,
        val createDate: String,
        val comments: Int,
        val likes: Int,
        val views: Int,
        val isSingleImage: Boolean
    )
}
