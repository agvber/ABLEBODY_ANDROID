package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.dto.request.FCMTokenAndAppVersionUpdateRequest
import com.smilehunter.ablebody.data.dto.request.NewUserCreateRequest
import com.smilehunter.ablebody.data.dto.request.RefreshTokenRequest
import com.smilehunter.ablebody.data.dto.request.SMSCheckRequest
import com.smilehunter.ablebody.data.dto.request.SMSSendRequest
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
import com.smilehunter.ablebody.data.dto.response.ReadBookmarkCodyResponse
import com.smilehunter.ablebody.data.dto.response.ReadBookmarkItemResponse
import com.smilehunter.ablebody.data.dto.response.RefreshTokenResponse
import com.smilehunter.ablebody.data.dto.response.SearchCodyResponse
import com.smilehunter.ablebody.data.dto.response.SearchItemResponse
import com.smilehunter.ablebody.data.dto.response.SendSMSResponse
import com.smilehunter.ablebody.data.dto.response.StringResponse
import com.smilehunter.ablebody.data.dto.response.UniSearchResponse
import com.smilehunter.ablebody.data.dto.response.UserDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkAPI {

    /** Onboarding **/

    @POST("/api/onboarding/send-sms")
    fun sendSMS(
        @Body smsSendRequest: SMSSendRequest
    ): Call<SendSMSResponse>

    @POST("/api/onboarding/check-sms")
    fun checkSMS(
        @Body smsCheckRequest: SMSCheckRequest
    ): Call<CheckSMSResponse>

    @GET("/api/onboarding/check-nickname/{nickname}")
    fun checkNickname(
        @Path(value = "nickname") nickname: String
    ): Call<StringResponse>

    @POST("/api/onboarding/new-user")
    fun createNewUser(
        @Body newUserCreateRequest: NewUserCreateRequest
    ): Call<NewUserCreateResponse>

    @POST("/api/onboarding/refresh")
    fun getRefreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): Call<RefreshTokenResponse>

    @GET("/api/onboarding/splash")
    fun getUserData(): Call<UserDataResponse>

    @GET("/api/onboarding/dummy")
    fun getDummyToken(): Call<StringResponse>

    @POST("/api/onboarding/fcm-appVersion")
    fun updateFCMTokenAndAppVersion(
        @Body fcmTokenAndAppVersionUpdateRequest: FCMTokenAndAppVersionUpdateRequest
    ): Call<FCMTokenAndAppVersionUpdateResponse>

    /** Brand **/

    @GET("/api/brand/main")
    fun brandMain(
        @Query("sort") sort: SortingMethod
    ): Call<BrandMainResponse>

    @GET("/api/brand/detail/item")
    fun brandDetailItem(
        @Query("sort") sort: SortingMethod,
        @Query("brandId") brandId: Long,
        @Query("itemGender") itemGender: ItemGender,
        @Query("parentCategory") parentCategory: ItemParentCategory,
        @Query("childCategory") childCategory: ItemChildCategory?,
        @Query("page") page: Int?,
        @Query("size") size: Int?

    ): Call<BrandDetailItemResponse>

    @GET("/api/brand/detail/cody")
    fun brandDetailCody(
        @Query("brandId") brandId: Long,
        @Query("gender") gender: String,
        @Query("category") category: String,
        @Query("height1") personHeightRangeStart: Int?,
        @Query("height2") personHeightRangeEnd: Int?,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
    ): Call<BrandDetailCodyResponse>

    /** Bookmark **/

    @POST("/api/bookmark/item")
    fun addBookmarkItem(
        @Query("itemId") itemID: Long
    ): Call<AddBookmarkItemResponse>

    @GET("/api/bookmark/item")
    fun readBookmarkItem(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Call<ReadBookmarkItemResponse>

    @DELETE("/api/bookmark/item")
    fun deleteBookmarkItem(
        @Query("itemId") itemID: Long
    ): Call<DeleteBookmarkItemResponse>

    @POST("/api/bookmark/cody")
    fun addBookmarkCody(
        @Query("codyId") itemID: Long
    ): Call<AddBookmarkCodyResponse>

    @GET("/api/bookmark/cody")
    fun readBookmarkCody(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Call<ReadBookmarkCodyResponse>

    @DELETE("/api/bookmark/cody")
    fun deleteBookmarkCody(
        @Query("codyId") codyID: Long
    ): Call<DeleteBookmarkCodyResponse>

    @GET("/api/find/new-item")
    fun findItem(
        @Query("itemGender") itemGender: ItemGender,
        @Query("parentCategory") parentCategory: ItemParentCategory,
        @Query("childCategory") childCategory: ItemChildCategory? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Call<FindItemResponse>

    @GET("/api/find/style")
    fun findCody(
        @Query("gender") genders: String,
        @Query("category") category: String,
        @Query("height1") personHeightRangeStart: Int?,
        @Query("height2") personHeightRangeEnd: Int?,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Call<FindCodyResponse>

    @GET("/api/search/uni")
    suspend fun uniSearch(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): UniSearchResponse

    @GET("/api/search/item")
    suspend fun searchItem(
        @Query("sort") sort: SortingMethod,
        @Query("keyword") keyword: String,
        @Query("itemGender") itemGender: ItemGender,
        @Query("parentCategory") parentCategory: ItemParentCategory,
        @Query("childCategory") childCategory: ItemChildCategory? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): SearchItemResponse

    @GET("/api/search/cody")
    suspend fun searchCody(
        @Query("keyword") keyword: String,
        @Query("gender") genders: String,
        @Query("category") category: String,
        @Query("height1") personHeightRangeStart: Int? = null,
        @Query("height2") personHeightRangeEnd: Int? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): SearchCodyResponse

    @GET("/api/my/noti")
    suspend fun getMyNoti(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 30
    ): GetMyNotiResponse

    @POST("/api/my/noti")
    suspend fun checkMyNoti(
        @Query("id") id: Long
    ): CheckMyNotiResponse
}