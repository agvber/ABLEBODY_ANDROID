package com.example.ablebody_android.data.repository

import com.example.ablebody_android.data.dto.response.CheckSMSResponse
import com.example.ablebody_android.data.dto.response.FCMTokenAndAppVersionUpdateResponse
import com.example.ablebody_android.data.dto.response.NewUserCreateResponse
import com.example.ablebody_android.data.dto.response.SendSMSResponse
import com.example.ablebody_android.data.dto.response.StringResponse
import com.example.ablebody_android.data.dto.response.UserDataResponse
import retrofit2.Response

interface OnboardingRepository {
    suspend fun sendSMS(
        phoneNumber: String,
        isNotTestMessage: Boolean = true
    ): Response<SendSMSResponse>
    suspend fun checkSMS(
        phoneConfirmId: Long,
        verifyingCode: String
    ): Response<CheckSMSResponse>
    suspend fun checkNickname(nickname: String): Response<StringResponse>
    suspend fun createNewUser(
        gender: com.example.ablebody_android.data.dto.Gender,
        nickname: String,
        profileImage: Int,
        verifyingCode: String,
        agreeRequiredConsent: Boolean,
        agreeMarketingConsent: Boolean
    ): Response<NewUserCreateResponse>
    suspend fun getUserData(): Response<UserDataResponse>
    suspend fun updateFCMTokenAndAppVersion(fcmToken: String, appVersion: String): Response<FCMTokenAndAppVersionUpdateResponse>
}