package com.example.ablebody_android

import com.example.ablebody_android.retrofit.NetworkAPI
import com.example.ablebody_android.retrofit.NetworkService
import com.example.ablebody_android.retrofit.dto.request.FCMTokenAndAppVersionUpdateRequest
import com.example.ablebody_android.retrofit.dto.request.NewUserCreateRequest
import com.example.ablebody_android.retrofit.dto.request.SMSCheckRequest
import com.example.ablebody_android.retrofit.dto.request.SMSSendRequest
import com.example.ablebody_android.retrofit.dto.request.TokenRefreshRequest
import com.example.ablebody_android.retrofit.dto.response.StringResponse
import com.example.ablebody_android.retrofit.dto.response.CheckSMSResponse
import com.example.ablebody_android.retrofit.dto.response.FCMTokenAndAppVersionUpdateResponse
import com.example.ablebody_android.retrofit.dto.response.NewUserCreateResponse
import com.example.ablebody_android.retrofit.dto.response.SendSMSResponse
import com.example.ablebody_android.retrofit.dto.response.TokenRefreshResponse
import com.example.ablebody_android.retrofit.dto.response.UserDataResponse
import retrofit2.Response

class NetworkRepository {

    private val networkService = NetworkService.retrofit.create(NetworkAPI::class.java)


    fun sendSMS(phoneNumber: String, isNotTestMessage: Boolean = true): Response<SendSMSResponse> {
        val smsSendRequest = SMSSendRequest(phoneNumber, isNotTestMessage)
        return networkService.sendSMS(smsSendRequest).execute()
    }

    fun checkSMS(phoneConfirmId: Long, verifyingCode: String): Response<CheckSMSResponse> {
        val smsCheckRequest = SMSCheckRequest(phoneConfirmId, verifyingCode)
        return networkService.checkSMS(smsCheckRequest).execute()
    }

    fun checkNickname(nickname: String): Response<StringResponse> =
        networkService.checkNickname(nickname).execute()

    fun createNewUser(
        gender: Gender,
        nickname: String,
        profileImage: Int,
        verifyingCode: String,
        agreeRequiredConsent: Boolean,
        agreeMarketingConsent: Boolean
    ): Response<NewUserCreateResponse> {
        val newUserCreateRequest = NewUserCreateRequest(
            gender = gender.name,
            nickname = nickname,
            profileImage = profileImage,
            verifyingCode = verifyingCode,
            agreeRequiredConsent = agreeRequiredConsent,
            agreeMarketingConsent = agreeMarketingConsent
        )
        return networkService.createNewUser(newUserCreateRequest).execute()
    }

    fun refreshToken(refreshToken: String): Response<TokenRefreshResponse> {
        val tokenRefreshResponseData = TokenRefreshRequest(refreshToken)
        return networkService.refreshToken(tokenRefreshResponseData).execute()
    }

    fun getUserData(authToken: String): Response<UserDataResponse> {
        val header = "Bearer $authToken"
        return networkService.getUserData(header).execute()
    }

    fun getDummyToken(): Response<StringResponse> = networkService.getDummyToken().execute()

    fun updateFCMTokenAndAppVersion(
        authToken: String,
        fcmToken: String,
        appVersion: String
    ): Response<FCMTokenAndAppVersionUpdateResponse> {
        val header = "Bearer $authToken"
        val request = FCMTokenAndAppVersionUpdateRequest(fcmToken = fcmToken, appVersion = appVersion)
        return networkService.updateFCMTokenAndAppVersion(header, request).execute()
    }
}