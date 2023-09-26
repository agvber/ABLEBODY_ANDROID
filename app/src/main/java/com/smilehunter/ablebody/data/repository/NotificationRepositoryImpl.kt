package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.CheckMyNotiResponse
import com.smilehunter.ablebody.data.dto.response.GetMyNotiResponse
import com.smilehunter.ablebody.network.NetworkService
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): NotificationRepository {
    override suspend fun getMyNoti(page: Int, size: Int): GetMyNotiResponse =
        networkService.getMyNoti(page, size)

    override suspend fun checkMyNoti(id: Long): CheckMyNotiResponse =
        networkService.checkMyNoti(id)

    override suspend fun checkAllMyNoti(): CheckMyNotiResponse =
        networkService.checkMyNoti(-1)
}