package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.CreatorDetailLikeUsersResponse
import com.smilehunter.ablebody.model.LikedLocations

interface LikeListRepository {

    suspend fun creatorDetailLikeUsers(
        where: LikedLocations,
        id: Long
    ): CreatorDetailLikeUsersResponse
}