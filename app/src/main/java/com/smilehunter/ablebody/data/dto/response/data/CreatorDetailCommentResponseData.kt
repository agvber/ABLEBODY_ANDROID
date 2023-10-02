package com.smilehunter.ablebody.data.dto.response.data

import com.smilehunter.ablebody.data.dto.NetworkAuthorityName

data class CreatorDetailCommentResponseData(
    val type: CommentReplyType,
    val createDate: String,
    val modifiedDate: String,
    val id: Long,
    val writer: User,
    val contents: String,
    val likes: Int,
    val likeUsers: List<User>,
    val parentId: Long?
) {
    enum class CommentReplyType {
        COMMENT, REPLY
    }
    data class User(
        val createDate: String,
        val modifiedDate: String,
        val gender: Gender,
        val uid: String,
        val nickname: String,
        val name: String,
        val height: Int?,
        val weight: Int?,
        val job: String?,
        val profileUrl: String?,
        val introduction: String?,
        val creatorPoint: Int,
        val authorities: Set<Authority>
    )

    enum class Gender { MALE, FEMALE }

    data class Authority(
        val authorityName: NetworkAuthorityName
    )
}