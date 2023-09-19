package com.example.ablebody_android.network

import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.data.dto.request.FCMTokenAndAppVersionUpdateRequest
import com.example.ablebody_android.data.dto.request.NewUserCreateRequest
import com.example.ablebody_android.data.dto.request.RefreshTokenRequest
import com.example.ablebody_android.data.dto.request.SMSCheckRequest
import com.example.ablebody_android.data.dto.request.SMSSendRequest
import com.example.ablebody_android.data.dto.response.AddBookmarkCodyResponse
import com.example.ablebody_android.data.dto.response.AddBookmarkItemResponse
import com.example.ablebody_android.data.dto.response.BrandDetailCodyResponse
import com.example.ablebody_android.data.dto.response.BrandDetailItemResponse
import com.example.ablebody_android.data.dto.response.BrandMainResponse
import com.example.ablebody_android.data.dto.response.CheckSMSResponse
import com.example.ablebody_android.data.dto.response.DeleteBookmarkCodyResponse
import com.example.ablebody_android.data.dto.response.DeleteBookmarkItemResponse
import com.example.ablebody_android.data.dto.response.FCMTokenAndAppVersionUpdateResponse
import com.example.ablebody_android.data.dto.response.FindCodyResponse
import com.example.ablebody_android.data.dto.response.FindItemResponse
import com.example.ablebody_android.data.dto.response.NewUserCreateResponse
import com.example.ablebody_android.data.dto.response.ReadBookmarkCodyResponse
import com.example.ablebody_android.data.dto.response.ReadBookmarkItemResponse
import com.example.ablebody_android.data.dto.response.RefreshTokenResponse
import com.example.ablebody_android.data.dto.response.SearchCodyResponse
import com.example.ablebody_android.data.dto.response.SearchItemResponse
import com.example.ablebody_android.data.dto.response.SendSMSResponse
import com.example.ablebody_android.data.dto.response.StringResponse
import com.example.ablebody_android.data.dto.response.UniSearchResponse
import com.example.ablebody_android.data.dto.response.UserDataResponse
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
    fun uniSearch(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Call<UniSearchResponse>

    @GET("/api/search/item")
    fun searchItem(
        @Query("sort") sort: SortingMethod,
        @Query("keyword") keyword: String,
        @Query("itemGender") itemGender: ItemGender,
        @Query("parentCategory") parentCategory: ItemParentCategory,
        @Query("childCategory") childCategory: ItemChildCategory? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Call<SearchItemResponse>

    @GET("/api/search/cody")
    fun searchCody(
        @Query("keyword") keyword: String,
        @Query("gender") genders: String,
        @Query("category") category: String,
        @Query("height1") personHeightRangeStart: Int? = null,
        @Query("height2") personHeightRangeEnd: Int? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Call<SearchCodyResponse>

}