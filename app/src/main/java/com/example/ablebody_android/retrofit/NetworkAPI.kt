package com.example.ablebody_android.retrofit

import com.example.ablebody_android.Gender
import com.example.ablebody_android.HomeCategory
import com.example.ablebody_android.ItemChildCategory
import com.example.ablebody_android.ItemGender
import com.example.ablebody_android.ItemParentCategory
import com.example.ablebody_android.SortingMethod
import com.example.ablebody_android.retrofit.dto.request.FCMTokenAndAppVersionUpdateRequest
import com.example.ablebody_android.retrofit.dto.request.NewUserCreateRequest
import com.example.ablebody_android.retrofit.dto.request.SMSCheckRequest
import com.example.ablebody_android.retrofit.dto.response.SendSMSResponse
import com.example.ablebody_android.retrofit.dto.request.SMSSendRequest
import com.example.ablebody_android.retrofit.dto.request.RefreshTokenRequest
import com.example.ablebody_android.retrofit.dto.response.BrandDetaiItemResponse
import com.example.ablebody_android.retrofit.dto.response.BrandDetailCodyResponse
import com.example.ablebody_android.retrofit.dto.response.BrandMainResponse
import com.example.ablebody_android.retrofit.dto.response.StringResponse
import com.example.ablebody_android.retrofit.dto.response.CheckSMSResponse
import com.example.ablebody_android.retrofit.dto.response.FCMTokenAndAppVersionUpdateResponse
import com.example.ablebody_android.retrofit.dto.response.NewUserCreateResponse
import com.example.ablebody_android.retrofit.dto.response.RefreshTokenResponse
import com.example.ablebody_android.retrofit.dto.response.UserDataResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkAPI {

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

    @GET("/api/brand/main")
    fun brandMain(
        @Header("Authorization") authorizationHeader: String,
        @Query("sort") sort: SortingMethod
    ): Call<BrandMainResponse>

    @GET("/api/brand/detail/item")
    fun brandDetaiItem(
        @Header("Authorization") authorizationHeader: String,
        @Query("sort") sort: SortingMethod,
        @Query("brandId") brandId: Long,
        @Query("itemGender") itemGender: ItemGender,
        @Query("parentCategory") parentCategory: ItemParentCategory,
        @Query("childCategory") childCategory: ItemChildCategory?,
        @Query("page") page: Int?,
        @Query("size") size: Int?

    ): Call<BrandDetaiItemResponse>

    @GET("/api/brand/detail/cody")
    fun brandDetailCody(
        @Header("Authorization") authorizationHeader: String,
        @Query("brandId") brandId: Long,
        @Query("gender") gender: List<Gender>,
        @Query("category") category: List<HomeCategory>,
        @Query("height1") height1: Int?,
        @Query("height2") height2: Int?,
        @Query("page") page: Int?,
        @Query("size") size: Int?

    ): Call<BrandDetailCodyResponse>

}