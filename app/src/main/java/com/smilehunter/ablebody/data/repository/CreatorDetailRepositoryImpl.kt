package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.data.CreatorDetailResponseData
import com.smilehunter.ablebody.network.NetworkService
import javax.inject.Inject

class CreatorDetailRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): CreatorDetailRepository {
    override suspend fun getCreatorDetailData(id: Long): CreatorDetailResponseData {
        return networkService.creatorDetail(id).data!!
    }

    override suspend fun deleteCreatorDetailPage(id: Long) {
        networkService.creatorDetailDelete(id)
    }

    override suspend fun toggleLike(id: Long) {
        networkService.creatorDetailLikeBoard(id)
    }
}