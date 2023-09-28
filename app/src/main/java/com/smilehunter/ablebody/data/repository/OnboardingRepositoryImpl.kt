package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.response.CheckSMSResponse
import com.smilehunter.ablebody.data.dto.response.NewUserCreateResponse
import com.smilehunter.ablebody.data.dto.response.SendSMSResponse
import com.smilehunter.ablebody.data.dto.response.StringResponse
import com.smilehunter.ablebody.data.dto.response.UserDataResponse
import com.smilehunter.ablebody.network.NetworkService
import com.smilehunter.ablebody.sharedPreferences.TokenSharedPreferences
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
}