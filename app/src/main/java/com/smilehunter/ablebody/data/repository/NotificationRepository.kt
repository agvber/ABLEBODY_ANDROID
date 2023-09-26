package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.CheckMyNotiResponse
import com.smilehunter.ablebody.data.dto.response.GetMyNotiResponse

interface NotificationRepository {

    suspend fun getMyNoti(page: Int = 0, size: Int = 30): GetMyNotiResponse
    suspend fun checkMyNoti(id: Long): CheckMyNotiResponse
    suspend fun checkAllMyNoti(): CheckMyNotiResponse
}