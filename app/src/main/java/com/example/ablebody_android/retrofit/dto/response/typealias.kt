package com.example.ablebody_android.retrofit.dto.response

import com.example.ablebody_android.retrofit.dto.response.data.BrandDetaiItemResponseData
import com.example.ablebody_android.retrofit.dto.response.data.BrandDetailCodyResponseData
import com.example.ablebody_android.retrofit.dto.response.data.BrandMainResponseData
import com.example.ablebody_android.retrofit.dto.response.data.FCMTokenAndAppVersionUpdateResponseData
import com.example.ablebody_android.retrofit.dto.response.data.NewUserCreateResponseData
import com.example.ablebody_android.retrofit.dto.response.data.RefreshTokenResponseData
import com.example.ablebody_android.retrofit.dto.response.data.SMSCheckResponseData
import com.example.ablebody_android.retrofit.dto.response.data.SMSSendResponseData
import com.example.ablebody_android.retrofit.dto.response.data.UserDataResponseData

typealias StringResponse = AbleBodyResponse<String>
typealias SendSMSResponse = AbleBodyResponse<SMSSendResponseData>
typealias CheckSMSResponse = AbleBodyResponse<SMSCheckResponseData>
typealias NewUserCreateResponse = AbleBodyResponse<NewUserCreateResponseData>
typealias RefreshTokenResponse = AbleBodyResponse<RefreshTokenResponseData>
typealias UserDataResponse = AbleBodyResponse<UserDataResponseData>
typealias FCMTokenAndAppVersionUpdateResponse = AbleBodyResponse<FCMTokenAndAppVersionUpdateResponseData>
typealias BrandMainResponse = AbleBodyResponse<BrandMainResponseData>
typealias BrandDetaiItemResponse = AbleBodyResponse<BrandDetaiItemResponseData>
typealias BrandDetailCodyResponse = AbleBodyResponse<BrandDetailCodyResponseData>

