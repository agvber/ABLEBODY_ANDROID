package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.data.CreatorDetailLikeUsersResponseData
import com.smilehunter.ablebody.network.NetworkService
import javax.inject.Inject

class LikeListRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): LikeListRepository {
    override suspend fun creatorDetailLikeUsersBoard(id: Long): List<CreatorDetailLikeUsersResponseData> =
        networkService.creatorDetailLikeUsersBoard(id).data!!

    override suspend fun creatorDetailLikeUsersComment(id: Long): List<CreatorDetailLikeUsersResponseData> =
        networkService.creatorDetailLikeUsersComment(id).data!!

    override suspend fun creatorDetailLikeUsersReply(id: Long): List<CreatorDetailLikeUsersResponseData> =
        networkService.creatorDetailLikeUsersReply(id).data!!
}