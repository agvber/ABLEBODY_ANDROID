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
import com.smilehunter.ablebody.data.dto.response.CreatorDetailCommentResponse
import com.smilehunter.ablebody.data.dto.response.CreatorDetailDeleteCommentReplyResponse
import com.smilehunter.ablebody.data.dto.response.CreatorDetailLikeResponse
import com.smilehunter.ablebody.data.dto.response.CreatorDetailLikeUsersResponse
import com.smilehunter.ablebody.data.dto.response.CreatorDetailReplyResponse
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
import retrofit2.Response

interface NetworkService {

    /** Onboarding **/

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


    /** Brand **/

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

    /** Bookmark **/

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

    /** Find **/

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


    /** Search **/

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

    /** Notification **/

    suspend fun getMyNoti(
        page: Int = 0,
        size: Int = 30
    ): GetMyNotiResponse

    suspend fun checkMyNoti(
        id: Long
    ): CheckMyNotiResponse

    /** Creator **/

    suspend fun creatorDetailLikeBoard(
        id: Long
    ): CreatorDetailLikeResponse

    suspend fun creatorDetailLikeComment(
        id: Long
    ): CreatorDetailLikeResponse

    suspend fun creatorDetailLikeReply(
        id: Long
    ): CreatorDetailLikeResponse

    suspend fun creatorDetailLikeUsersBoard(
        id: Long
    ): CreatorDetailLikeUsersResponse

    suspend fun creatorDetailLikeUsersComment(
        id: Long
    ): CreatorDetailLikeUsersResponse

    suspend fun creatorDetailLikeUsersReply(
        id: Long
    ): CreatorDetailLikeUsersResponse

    suspend fun creatorDetailComment(
        id: Long,
        content: String
    ): CreatorDetailCommentResponse

    suspend fun creatorDetailReply(
        id: Long,
        content: String
    ): CreatorDetailReplyResponse

    suspend fun creatorDetailDeleteComment(
        id: Long
    ): CreatorDetailDeleteCommentReplyResponse

    suspend fun creatorDetailDeleteReply(
        id: Long
    ): CreatorDetailDeleteCommentReplyResponse
}