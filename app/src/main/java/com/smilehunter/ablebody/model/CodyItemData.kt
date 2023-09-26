package com.smilehunter.ablebody.model

data class CodyItemData(
    val content: List<Item>,
    val totalPages: Int,
    val last: Boolean,
    val number: Int,
    val first: Boolean,
) {
    data class Item(
        val id: Long,
        val imageURL: String,
        val createDate: String,
        val comments: Int,
        val likes: Int,
        val views: Int,
        val isSingleImage: Boolean
    )
}
