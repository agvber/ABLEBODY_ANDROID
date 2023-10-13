package com.smilehunter.ablebody.model

import com.smilehunter.ablebody.utils.calculateElapsedTime

data class CommentListData(
    val type: CommentType,
    val createDate: String,
    val modifiedDate: String,
    val id: Long,
    val writer: User,
    val contents: String,
    val likeCount: Int,
    val parentID: Long?,
    val isLiked: Boolean
) {
    val elapsedTime = calculateElapsedTime(createDate)
    enum class CommentType { COMMENT, REPLY }

    data class User(
        val uid: String,
        val nickname: String,
        val name: String,
        val profileUrl: String?,
    )
}
