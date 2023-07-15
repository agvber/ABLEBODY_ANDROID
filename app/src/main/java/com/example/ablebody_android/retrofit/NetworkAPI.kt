package com.example.ablebody_android.retrofit

import com.example.ablebody_android.retrofit.dto.request.FCMTokenAndAppVersionUpdateRequest
import com.example.ablebody_android.retrofit.dto.request.NewUserCreateRequest
import com.example.ablebody_android.retrofit.dto.request.SMSCheckRequest
import com.example.ablebody_android.retrofit.dto.response.SendSMSResponse
import com.example.ablebody_android.retrofit.dto.request.SMSSendRequest
import com.example.ablebody_android.retrofit.dto.request.RefreshTokenRequest
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
    fun getUserData(
        @Header("Authorization") authorizationHeader: String
    ): Call<UserDataResponse>

    @GET("/api/onboarding/dummy")
    fun getDummyToken(): Call<StringResponse>

    @POST("/api/onboarding/fcm-appVersion")
    fun updateFCMTokenAndAppVersion(
        @Header("Authorization") authorizationHeader: String,
        @Body fcmTokenAndAppVersionUpdateRequest: FCMTokenAndAppVersionUpdateRequest
    ): Call<FCMTokenAndAppVersionUpdateResponse>
}