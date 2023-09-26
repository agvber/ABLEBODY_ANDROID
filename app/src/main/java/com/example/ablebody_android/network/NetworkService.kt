package com.example.ablebody_android.network

import com.example.ablebody_android.data.dto.Gender
import com.example.ablebody_android.data.dto.HomeCategory
import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.data.dto.response.AbleBodyResponse
import com.example.ablebody_android.data.dto.response.AddBookmarkCodyResponse
import com.example.ablebody_android.data.dto.response.AddBookmarkItemResponse
import com.example.ablebody_android.data.dto.response.BrandDetailCodyResponse
import com.example.ablebody_android.data.dto.response.BrandDetailItemResponse
import com.example.ablebody_android.data.dto.response.BrandMainResponse
import com.example.ablebody_android.data.dto.response.CheckMyNotiResponse
import com.example.ablebody_android.data.dto.response.CheckSMSResponse
import com.example.ablebody_android.data.dto.response.DeleteBookmarkCodyResponse
import com.example.ablebody_android.data.dto.response.DeleteBookmarkItemResponse
import com.example.ablebody_android.data.dto.response.FCMTokenAndAppVersionUpdateResponse
import com.example.ablebody_android.data.dto.response.FindCodyResponse
import com.example.ablebody_android.data.dto.response.FindItemResponse
import com.example.ablebody_android.data.dto.response.GetMyNotiResponse
import com.example.ablebody_android.data.dto.response.NewUserCreateResponse
import com.example.ablebody_android.data.dto.response.RefreshTokenResponse
import com.example.ablebody_android.data.dto.response.SearchCodyResponse
import com.example.ablebody_android.data.dto.response.SearchItemResponse
import com.example.ablebody_android.data.dto.response.SendSMSResponse
import com.example.ablebody_android.data.dto.response.StringResponse
import com.example.ablebody_android.data.dto.response.UniSearchResponse
import com.example.ablebody_android.data.dto.response.UserDataResponse
import com.example.ablebody_android.data.dto.response.data.ReadBookmarkCodyData
import com.example.ablebody_android.data.dto.response.data.ReadBookmarkItemData
import retrofit2.Response

interface NetworkService {
    suspend fun sendSMS(phoneNumber: String, isNotTestMessage: Boolean = true): Response<SendSMSResponse>

    suspend fun checkSMS(phoneConfirmId: Long, verifyingCode: String): Response<CheckSMSResponse>

    suspend fun checkNickname(nickname: String): Response<StringResponse>

    suspend fun createNewUser(
        gender: Gender,
        nickname: String,
        profileImage: Int,
        verifyingCode: String,
        agreeRequiredConsent: Boolean,
        agreeMarketingConsent: Boolean
    ): Response<NewUserCreateResponse>

    suspend fun getRefreshToken(refreshToken: String): Response<RefreshTokenResponse>

    suspend fun getUserData(): Response<UserDataResponse>

    suspend fun getDummyToken(): Response<StringResponse>

    suspend fun updateFCMTokenAndAppVersion(
        fcmToken: String,
        appVersion: String
    ): Response<FCMTokenAndAppVersionUpdateResponse>

    suspend fun brandMain(sort: SortingMethod): Response<BrandMainResponse>

    suspend fun brandDetailItem(
        sort: SortingMethod,
        brandId: Long,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory? = null,
        page: Int? = 0,
        size: Int? = 20
    ): Response<BrandDetailItemResponse>

    suspend fun brandDetailCody(
        brandId: Long,
        gender: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int? = null,
        personHeightRangeEnd: Int? = null,
        page: Int? = 0,
        size: Int? = 20
    ): Response<BrandDetailCodyResponse>

    suspend fun addBookmarkItem(itemID: Long): Response<AddBookmarkItemResponse>

    suspend fun readBookmarkItem(
        page: Int = 0,
        size: Int = 20
    ): Response<AbleBodyResponse<ReadBookmarkItemData>>

    suspend fun deleteBookmarkItem(itemID: Long): Response<DeleteBookmarkItemResponse>

    suspend fun addBookmarkCody(itemID: Long): Response<AddBookmarkCodyResponse>

    suspend fun readBookmarkCody(
        page: Int = 0,
        size: Int = 20
    ): Response<AbleBodyResponse<ReadBookmarkCodyData>>

    suspend fun deleteBookmarkCody(itemID: Long): Response<DeleteBookmarkCodyResponse>
    suspend fun findItem(
        sort: SortingMethod,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory? = null,
        page: Int = 0,
        size: Int = 20
    ): Response<FindItemResponse>

    suspend fun findCody(
        genders: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int? = null,
        personHeightRangeEnd: Int? = null,
        page: Int = 0,
        size: Int = 20
    ): Response<FindCodyResponse>

    suspend fun uniSearch(
        keyword: String,
        page: Int = 0,
        size: Int = 10
    ): UniSearchResponse

    suspend fun searchItem(
        sort: SortingMethod,
        keyword: String,
        itemGender: ItemGender,
        parentCategory: ItemParentCategory,
        childCategory: ItemChildCategory? = null,
        page: Int = 0,
        size: Int = 20
    ): SearchItemResponse

    suspend fun searchCody(
        keyword: String,
        genders: List<Gender>,
        category: List<HomeCategory>,
        personHeightRangeStart: Int? = null,
        personHeightRangeEnd: Int? = null,
        page: Int = 0,
        size: Int = 20
    ): SearchCodyResponse

    suspend fun getMyNoti(
        page: Int = 0,
        size: Int = 30
    ): GetMyNotiResponse

    suspend fun checkMyNoti(
        id: Long
    ): CheckMyNotiResponse
}