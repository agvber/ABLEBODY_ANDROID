package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.data.CreatorDetailCommentResponseData
import com.smilehunter.ablebody.data.dto.response.data.CreatorDetailLikeUsersResponseData
import com.smilehunter.ablebody.data.dto.response.data.CreatorDetailReplyResponseData

interface CommentRepository {

    suspend fun creatorDetailLikeBoard(
        id: Long
    )

    suspend fun creatorDetailLikeComment(
        id: Long
    )

    suspend fun creatorDetailLikeReply(
        id: Long
    )

    suspend fun creatorDetailLikeUsersBoard(
        id: Long
    ): List<CreatorDetailLikeUsersResponseData>

    suspend fun creatorDetailLikeUsersComment(
        id: Long
    ): List<CreatorDetailLikeUsersResponseData>

    suspend fun creatorDetailLikeUsersReply(
        id: Long
    ): List<CreatorDetailLikeUsersResponseData>

    suspend fun creatorDetailComment(
        id: Long,
        content: String
    ): CreatorDetailCommentResponseData

    suspend fun creatorDetailReply(
        id: Long,
        content: String
    ): CreatorDetailReplyResponseData

    suspend fun creatorDetailDeleteComment(
        id: Long
    )

    suspend fun creatorDetailDeleteReply(
        id: Long
    )
}