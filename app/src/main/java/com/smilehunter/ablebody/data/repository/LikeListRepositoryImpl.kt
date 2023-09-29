package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.NetworkLikedLocations
import com.smilehunter.ablebody.data.dto.response.CreatorDetailLikeUsersResponse
import com.smilehunter.ablebody.model.LikedLocations
import com.smilehunter.ablebody.network.NetworkService
import javax.inject.Inject

class LikeListRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): LikeListRepository {
    override suspend fun creatorDetailLikeUsers(
        where: LikedLocations,
        id: Long
    ): CreatorDetailLikeUsersResponse =
        networkService.creatorDetailLikeUsers(
            where = when(where) {
                LikedLocations.BOARD -> NetworkLikedLocations.BOARD
                LikedLocations.COMMENT -> NetworkLikedLocations.COMMENT
                LikedLocations.REPLAY -> NetworkLikedLocations.REPLAY
            },
            id = id
        )
}