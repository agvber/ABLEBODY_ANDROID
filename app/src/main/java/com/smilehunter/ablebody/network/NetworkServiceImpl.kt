package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.HomeCategory
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.dto.response.AbleBodyResponse
import com.smilehunter.ablebody.data.dto.response.AddBookmarkCodyResponse
import com.smilehunter.ablebody.data.dto.response.AddBookmarkItemResponse
import com.smilehunter.ablebody.data.dto.response.BrandDetailCodyResponse
import com.smilehunter.ablebody.data.dto.response.BrandDetailItemResponse
import com.smilehunter.ablebody.data.dto.response.BrandMainResponse
import com.smilehunter.ablebody.data.dto.response.CheckMyNotiResponse
import com.smilehunter.ablebody.data.dto.response.CheckSMSResponse
import com.smilehunter.ablebody.data.dto.response.DeleteBookmarkCodyResponse
import com.smilehunter.ablebody.data.dto.response.DeleteBookmarkItemResponse
import com.smilehunter.ablebody.data.dto.response.FCMTokenAndAppVersionUpdateResponse
import com.smilehunter.ablebody.data.dto.response.FindCodyResponse
import com.smilehunter.ablebody.data.dto.response.FindItemResponse
import com.smilehunter.ablebody.data.dto.response.GetMyNotiResponse
import com.smilehunter.ablebody.data.dto.response.NewUserCreateResponse
import com.smilehunter.ablebody.data.dto.response.RefreshTokenResponse
import com.smilehunter.ablebody.data.dto.response.SearchCodyResponse
import com.smilehunter.ablebody.data.dto.response.SearchItemResponse
import com.smilehunter.ablebody.data.dto.response.SendSMSResponse
import com.smilehunter.ablebody.data.dto.response.StringResponse
import com.smilehunter.ablebody.data.dto.response.UniSearchResponse
import com.smilehunter.ablebody.data.dto.response.UserDataResponse
import com.smilehunter.ablebody.data.dto.response.data.ReadBookmarkCodyData
import com.smilehunter.ablebody.data.dto.response.data.ReadBookmarkItemData
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

const val MAIN_SERVER_URL = "https://aws.ablebody.im:50913"
const val TEST_SERVER_URL = "https://aws.ablebody.im:40913"

@Singleton
class NetworkServiceImpl @Inject constructor(
    okHttpClient: OkHttpClient
): NetworkService {

    private val retrofit = Retrofit.Builder().run {
        baseUrl(MAIN_SERVER_URL)
        addConverterFactory(GsonConverterFactory.create())
        client(okHttpClient)
        build()
    }

    private val networkAPI: NetworkAPI = retrofit.create(NetworkAPI::class.java)
    override suspend fun sendSMS(phoneNumber: String, isNotTestMessage: Boolean): Response<SendSMSResponse> {
        val smsSendRequest = com.smilehunter.ablebody.data.dto.request.SMSSendRequest(
            phoneNumber,
            isNotTestMessage
        )
        return networkAPI.sendSMS(smsSendRequest).execute()
    }

    override suspend fun checkSMS(phoneConfirmId: Long, verifyingCode: String): Response<CheckSMSResponse> {
        val smsCheckRequest = com.smilehunter.ablebody.data.dto.request.SMSCheckRequest(
            phoneConfirmId,
            verifyingCode
        )
        return networkAPI.checkSMS(smsCheckRequest).execute()
    }

    override suspend fun checkNickname(nickname: String): Response<StringResponse> =
        networkAPI.checkNickname(nickname).execute()

    override suspend fun createNewUser(
        gender: Gender,
        nickname: String,
        profileImage: Int,
        verifyingCode: String,
        agreeRequiredConsent: Boolean,
        agreeMarketingConsent: Boolean
    ): Response<NewUserCreateResponse> {
        val newUserCreateRequest =
            com.smilehunter.ablebody.data.dto.request.NewUserCreateRequest(
                gender = gender.name,
                nickname = nickname,
                profileImage = profileImage,
                verifyingCode = verifyingCode,
                agreeRequiredConsent = agreeRequiredConsent,
                agreeMarketingConsent = agreeMarketingConsent
            )
        return networkAPI.createNewUser(newUserCreateRequest).execute()
    }

    override suspend fun getRefreshToken(refreshToken: String): Response<RefreshTokenResponse> {
        val tokenRefreshResponseData =
            com.smilehunter.ablebody.data.dto.request.RefreshTokenRequest(refreshToken)
        return networkAPI.getRefreshToken(tokenRefreshResponseData).execute()
    }

    override suspend fun getUserData(): Response<UserDataResponse> = networkAPI.getUserData().execute()

    override suspend fun getDummyToken(): Response<StringResponse> = networkAPI.getDummyToken().execute()

    override suspend fun updateFCMTokenAndAppVersion(
        fcmToken: String,
        appVersion: String
    ): Response<FCMTokenAndAppVersionUpdateResponse> {
        val request =
            com.smilehunter.ablebody.data.dto.request.FCMTokenAndAppVersionUpdateRequest(
                fcmToken = fcmToken,
                appVersion = appVersion
            )
        return networkAPI.updateFCMTokenAndAppVersion(request).execute()
    }

    override suspend fun brandMain(
        sort: SortingMethod
    ): Response<BrandMainResponse> {
        return networkAPI.brandMain(sort).execute()
    }

    override suspend fun brandDetailItem(
        sort: SortingMethod,
        brandId: Long,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory?,
        page: Int?,
        size: Int?,
    ): Response<BrandDetailItemResponse> = networkAPI.brandDetailItem(
        sort,
        brandId,
        itemGender,
        parentCategory,
        childCategory,
        page,
        size
    ).execute()

    override suspend fun brandDetailCody(
        brandId: Long,
        gender: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int?,
        personHeightRangeEnd: Int?,
        page: Int?,
        size: Int?,
    ): Response<BrandDetailCodyResponse> = networkAPI.brandDetailCody(
        brandId,
        removeSquareBrackets(gender),
        removeSquareBrackets(category),
        personHeightRangeStart,
        personHeightRangeEnd,
        page,
        size
    ).execute()
    override suspend fun addBookmarkItem(itemID: Long): Response<AddBookmarkItemResponse> =
        networkAPI.addBookmarkItem(itemID).execute()

    override suspend fun readBookmarkItem(
        page: Int,
        size: Int,
    ): Response<AbleBodyResponse<ReadBookmarkItemData>> =
        networkAPI.readBookmarkItem(page, size).execute()

    override suspend fun deleteBookmarkItem(itemID: Long): Response<DeleteBookmarkItemResponse> =
        networkAPI.deleteBookmarkItem(itemID).execute()

    override suspend fun addBookmarkCody(itemID: Long): Response<AddBookmarkCodyResponse> =
        networkAPI.addBookmarkCody(itemID).execute()

    override suspend fun readBookmarkCody(
        page: Int,
        size: Int,
    ): Response<AbleBodyResponse<ReadBookmarkCodyData>> =
        networkAPI.readBookmarkCody(page, size).execute()

    override suspend fun deleteBookmarkCody(itemID: Long): Response<DeleteBookmarkCodyResponse> =
        networkAPI.deleteBookmarkCody(itemID).execute()

    override suspend fun findItem(
        sort: SortingMethod,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory?,
        page: Int,
        size: Int
    ): Response<FindItemResponse> =
        networkAPI.findItem(
            itemGender = itemGender,
            parentCategory = parentCategory,
            childCategory = childCategory,
            page = page,
            size = size
        ).execute()

    override suspend fun findCody(
        genders: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int?,
        personHeightRangeEnd: Int?,
        page: Int,
        size: Int,
    ): Response<FindCodyResponse> =
        networkAPI.findCody(
            genders = removeSquareBrackets(genders),
            category = removeSquareBrackets(category),
            personHeightRangeStart = personHeightRangeStart,
            personHeightRangeEnd = personHeightRangeEnd,
            page = page,
            size = size
        ).execute()

    override suspend fun uniSearch(
        keyword: String,
        page: Int,
        size: Int
    ): UniSearchResponse =
        networkAPI.uniSearch(keyword, page, size)

    override suspend fun searchItem(
        sort: SortingMethod,
        keyword: String,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory?,
        page: Int,
        size: Int
    ): SearchItemResponse =
        networkAPI.searchItem(sort, keyword, itemGender, parentCategory, childCategory, page, size)

    override suspend fun searchCody(
        keyword: String,
        genders: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int?,
        personHeightRangeEnd: Int?,
        page: Int,
        size: Int
    ): SearchCodyResponse =
        networkAPI.searchCody(
            keyword,
            removeSquareBrackets(genders),
            removeSquareBrackets(category),
            personHeightRangeStart,
            personHeightRangeEnd,
            page,
            size
        )

    override suspend fun getMyNoti(page: Int, size: Int): GetMyNotiResponse =
        networkAPI.getMyNoti(page, size)

    override suspend fun checkMyNoti(id: Long): CheckMyNotiResponse =
        networkAPI.checkMyNoti(id)
}