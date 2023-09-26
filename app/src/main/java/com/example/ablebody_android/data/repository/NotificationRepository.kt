package com.example.ablebody_android.data.repository

import com.example.ablebody_android.data.dto.response.CheckMyNotiResponse
import com.example.ablebody_android.data.dto.response.GetMyNotiResponse

interface NotificationRepository {

    suspend fun getMyNoti(page: Int = 0, size: Int = 30): GetMyNotiResponse
    suspend fun checkMyNoti(id: Long): CheckMyNotiResponse
    suspend fun checkAllMyNoti(): CheckMyNotiResponse
}