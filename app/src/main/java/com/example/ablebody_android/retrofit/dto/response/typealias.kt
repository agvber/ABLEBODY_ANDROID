package com.example.ablebody_android.retrofit.dto.response

import com.example.ablebody_android.retrofit.dto.response.data.FCMTokenAndAppVersionUpdateResponseData
import com.example.ablebody_android.retrofit.dto.response.data.SMSSendResponseData
import com.example.ablebody_android.retrofit.dto.response.data.SMSCheckResponseData
import com.example.ablebody_android.retrofit.dto.response.data.NewUserCreateResponseData
import com.example.ablebody_android.retrofit.dto.response.data.RefreshTokenResponseData
import com.example.ablebody_android.retrofit.dto.response.data.UserDataResponseData

typealias StringResponse = Response<String>
typealias SendSMSResponse = Response<SMSSendResponseData>
typealias CheckSMSResponse = Response<SMSCheckResponseData>
typealias NewUserCreateResponse = Response<NewUserCreateResponseData>
typealias RefreshTokenResponse = Response<RefreshTokenResponseData>
typealias UserDataResponse = Response<UserDataResponseData>
typealias FCMTokenAndAppVersionUpdateResponse = Response<FCMTokenAndAppVersionUpdateResponseData>