package com.example.ablebody_android.data.repository

import com.example.ablebody_android.data.dto.Gender
import com.example.ablebody_android.data.dto.response.CheckSMSResponse
import com.example.ablebody_android.data.dto.response.FCMTokenAndAppVersionUpdateResponse
import com.example.ablebody_android.data.dto.response.NewUserCreateResponse
import com.example.ablebody_android.data.dto.response.SendSMSResponse
import com.example.ablebody_android.data.dto.response.StringResponse
import com.example.ablebody_android.data.dto.response.UserDataResponse
import com.example.ablebody_android.network.NetworkService
import com.example.ablebody_android.sharedPreferences.TokenSharedPreferences
import retrofit2.Response
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val tokenSharedPreferences: TokenSharedPreferences
): OnboardingRepository {
    override suspend fun sendSMS(phoneNumber: String, isNotTestMessage: Boolean): Response<SendSMSResponse> {
        return networkService.sendSMS(phoneNumber, isNotTestMessage)
    }

    override suspend fun checkSMS(phoneConfirmId: Long, verifyingCode: String): Response<CheckSMSResponse> {
        return networkService.checkSMS(phoneConfirmId, verifyingCode)
            .also { response ->
                response.body()?.data?.tokens?.let { token ->
                    tokenSharedPreferences.putAuthToken(token.authToken)
                    tokenSharedPreferences.putRefreshToken(token.refreshToken)
                }
            }
    }

    override suspend fun checkNickname(nickname: String): Response<StringResponse> =
        networkService.checkNickname(nickname)

    override suspend fun createNewUser(
        gender: Gender,
        nickname: String,
        profileImage: Int,
        verifyingCode: String,
        agreeRequiredConsent: Boolean,
        agreeMarketingConsent: Boolean
    ): Response<NewUserCreateResponse> {
        return networkService.createNewUser(
            gender = gender,
            nickname = nickname,
            profileImage = profileImage,
            verifyingCode = verifyingCode,
            agreeRequiredConsent = agreeRequiredConsent,
            agreeMarketingConsent = agreeMarketingConsent
        ).also { response ->
            response.body()?.data?.tokens?.let { token ->
                tokenSharedPreferences.putAuthToken(token.authToken)
                tokenSharedPreferences.putRefreshToken(token.refreshToken)
            }
        }
    }

    override suspend fun getUserData(): Response<UserDataResponse> = networkService.getUserData()

    override suspend fun updateFCMTokenAndAppVersion(
        fcmToken: String,
        appVersion: String
    ): Response<FCMTokenAndAppVersionUpdateResponse> {
        return networkService.updateFCMTokenAndAppVersion(fcmToken = fcmToken, appVersion = appVersion)
    }
}