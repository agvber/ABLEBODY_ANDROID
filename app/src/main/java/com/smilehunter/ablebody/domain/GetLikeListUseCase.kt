package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.response.data.CreatorDetailLikeUsersResponseData
import com.smilehunter.ablebody.data.repository.LikeListRepository
import com.smilehunter.ablebody.model.LikeListData
import com.smilehunter.ablebody.model.LikedLocations
import javax.inject.Inject

class GetLikeListUseCase @Inject constructor(
    private val likeListRepository: LikeListRepository
) {

    suspend operator fun invoke(
        likedLocations: LikedLocations,
        id: Long
    ): List<LikeListData> =
        when (likedLocations) {
            LikedLocations.BOARD -> likeListRepository.creatorDetailLikeUsersBoard(id)
            LikedLocations.COMMENT -> likeListRepository.creatorDetailLikeUsersComment(id)
            LikedLocations.REPLAY -> likeListRepository.creatorDetailLikeUsersReply(id)
        }
            .map { it.toDomain() }
}

private fun CreatorDetailLikeUsersResponseData.toDomain() =
    LikeListData(
        uid = uid,
        nickname = nickname,
        userName = name,
        profileImageURL = profileUrl
    )