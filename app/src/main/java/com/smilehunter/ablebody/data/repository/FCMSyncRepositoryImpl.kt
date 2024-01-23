package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.network.NetworkService
import javax.inject.Inject

class FCMSyncRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): FCMSyncRepository {
    override suspend fun updateFCMTokenAndAppVersion(fcmToken: String, appVersion: String) =
        networkService.updateFCMTokenAndAppVersion(fcmToken, appVersion).body()!!.data!!
}