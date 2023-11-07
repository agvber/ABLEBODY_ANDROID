package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.response.data.CreatorDetailResponseData
import com.smilehunter.ablebody.data.repository.CreatorDetailRepository
import com.smilehunter.ablebody.model.CommentListData
import javax.inject.Inject

class GetCommentListUseCase @Inject constructor(
    private val creatorDetailRepository: CreatorDetailRepository
) {

    suspend operator fun invoke(id: Long, uid: String): List<CommentListData> =
        creatorDetailRepository.getCreatorDetailData(id).commentAndReplies.map { it.toDomain(uid) }

}

private fun CreatorDetailResponseData.CommentOrReply.toDomain(uid: String) =
    CommentListData(
        type = when (type) {
            CreatorDetailResponseData.CommentOrReply.CommentReplyType.COMMENT ->
                CommentListData.CommentType.COMMENT
            CreatorDetailResponseData.CommentOrReply.CommentReplyType.REPLY ->
                CommentListData.CommentType.REPLY
        },
        createDate = createDate,
        modifiedDate = modifiedDate,
        id = id,
        writer = CommentListData.User(
            uid = writer.uid,
            nickname = writer.nickname,
            name = writer.name,
            profileUrl = writer.profileUrl
        ),
        contents = contents,
        likeCount = likes,
        parentID = parentId,
        isLiked = likeUsers.find { it.uid == uid } != null
    )