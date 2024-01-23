package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.ItemDetailResponse

interface ItemRepository {
    suspend fun itemDetail(id: Long): ItemDetailResponse
}