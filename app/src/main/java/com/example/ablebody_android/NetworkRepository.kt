package com.example.ablebody_android

import android.util.Log
import com.example.ablebody_android.retrofit.NetworkAPI
import com.example.ablebody_android.retrofit.NetworkService
import com.example.ablebody_android.retrofit.dto.request.FCMTokenAndAppVersionUpdateRequest
import com.example.ablebody_android.retrofit.dto.request.NewUserCreateRequest
import com.example.ablebody_android.retrofit.dto.request.RefreshTokenRequest
import com.example.ablebody_android.retrofit.dto.request.SMSCheckRequest
import com.example.ablebody_android.retrofit.dto.request.SMSSendRequest
import com.example.ablebody_android.retrofit.dto.response.AbleBodyResponse
import com.example.ablebody_android.retrofit.dto.response.BrandDetailItemResponse
import com.example.ablebody_android.retrofit.dto.response.BrandDetailCodyResponse
import com.example.ablebody_android.retrofit.dto.response.BrandMainResponse
import com.example.ablebody_android.retrofit.dto.response.CheckSMSResponse
import com.example.ablebody_android.retrofit.dto.response.FCMTokenAndAppVersionUpdateResponse
import com.example.ablebody_android.retrofit.dto.response.NewUserCreateResponse
import com.example.ablebody_android.retrofit.dto.response.RefreshTokenResponse
import com.example.ablebody_android.retrofit.dto.response.SendSMSResponse
import com.example.ablebody_android.retrofit.dto.response.StringResponse
import com.example.ablebody_android.retrofit.dto.response.UserDataResponse
import com.example.ablebody_android.retrofit.dto.response.data.ReadBookmarkCodyData
import com.example.ablebody_android.retrofit.dto.response.data.ReadBookmarkItemData
import retrofit2.Response

class NetworkRepository(
    tokenSharedPreferencesRepository: TokenSharedPreferencesRepository
) {

    private val retrofit = NetworkService.getInstance(
        tokenSharedPreferencesRepository, this
    )
    private val networkService = retrofit.create(NetworkAPI::class.java)


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

    fun getRefreshToken(refreshToken: String): Response<RefreshTokenResponse> {
        val tokenRefreshResponseData = RefreshTokenRequest(refreshToken)
        return networkService.getRefreshToken(tokenRefreshResponseData).execute()
    }

    fun invalidRefreshToken() {
        Log.d(this::class.java.name, "invalid refresh token")
    }

    fun getUserData(): Response<UserDataResponse> = networkService.getUserData().execute()

    fun getDummyToken(): Response<StringResponse> = networkService.getDummyToken().execute()

    fun updateFCMTokenAndAppVersion(
        fcmToken: String,
        appVersion: String
    ): Response<FCMTokenAndAppVersionUpdateResponse> {
        val request = FCMTokenAndAppVersionUpdateRequest(fcmToken = fcmToken, appVersion = appVersion)
        return networkService.updateFCMTokenAndAppVersion(request).execute()
    }

    fun brandMain(
        sort: SortingMethod
    ): Response<BrandMainResponse> {
        return networkService.brandMain(sort).execute()
    }

    fun brandDetailItem(
        sort: SortingMethod,
        brandId: Long,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory? = null,
        page: Int? = 0,
        size: Int? = 20
    ): Response<BrandDetailItemResponse> = networkService.brandDetailItem(
        sort,
        brandId,
        itemGender,
        parentCategory,
        childCategory,
        page,
        size
    ).execute()

    fun brandDetailCody(
        brandId: Long,
        gender: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int? = null,
        personHeightRangeEnd: Int? = null,
        page: Int? = 0,
        size: Int? = 20
    ): Response<BrandDetailCodyResponse> = networkService.brandDetailCody(
        brandId,
        removeSquareBrackets(gender),
        removeSquareBrackets(category),
        personHeightRangeStart,
        personHeightRangeEnd,
        page,
        size
    ).execute()

    private fun<T> removeSquareBrackets(list: List<T>) =
        list.joinToString (",","","",-1)

    fun addBookmarkItem(itemID: Long): Response<AbleBodyResponse<String>> = networkService.addBookmarkItem(itemID).execute()

    fun readBookmarkItem(
        page: Int = 0,
        size: Int = 20
    ): Response<AbleBodyResponse<ReadBookmarkItemData>> =
        networkService.readBookmarkItem(page, size).execute()

    fun deleteBookmarkItem(itemID: Long): Response<AbleBodyResponse<String>> =
        networkService.deleteBookmarkItem(itemID).execute()

    fun addBookmarkCody(itemID: Long): Response<AbleBodyResponse<String>> =
        networkService.addBookmarkCody(itemID).execute()

    fun readBookmarkCody(
        page: Int = 0,
        size: Int = 20
    ): Response<AbleBodyResponse<ReadBookmarkCodyData>> =
        networkService.readBookmarkCody(page, size).execute()

    fun deleteBookmarkCody(itemID: Long): Response<AbleBodyResponse<String>> =
        networkService.deleteBookmarkCody(itemID).execute()


}