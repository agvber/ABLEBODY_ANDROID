package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.data.FCMTokenAndAppVersionUpdateResponseData

interface FCMSyncRepository {
    suspend fun updateFCMTokenAndAppVersion(fcmToken: String, appVersion: String): FCMTokenAndAppVersionUpdateResponseData
}