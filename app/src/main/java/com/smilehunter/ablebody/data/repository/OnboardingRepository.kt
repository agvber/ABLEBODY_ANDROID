package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.CheckSMSResponse
import com.smilehunter.ablebody.data.dto.response.NewUserCreateResponse
import com.smilehunter.ablebody.data.dto.response.SendSMSResponse
import com.smilehunter.ablebody.data.dto.response.StringResponse
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
        gender: com.smilehunter.ablebody.data.dto.Gender,
        nickname: String,
        profileImage: Int,
        verifyingCode: String,
        agreeRequiredConsent: Boolean,
        agreeMarketingConsent: Boolean
    ): Response<NewUserCreateResponse>
}