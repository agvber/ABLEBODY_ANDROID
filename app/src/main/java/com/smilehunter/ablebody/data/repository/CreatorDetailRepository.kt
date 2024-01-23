package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.data.CreatorDetailResponseData

interface CreatorDetailRepository {

    suspend fun getCreatorDetailData(id: Long): CreatorDetailResponseData
    suspend fun deleteCreatorDetailPage(id: Long)
    suspend fun toggleLike(id: Long)
}