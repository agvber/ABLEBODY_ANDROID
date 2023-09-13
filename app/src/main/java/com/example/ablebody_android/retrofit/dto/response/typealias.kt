package com.example.ablebody_android.retrofit.dto.response

import com.example.ablebody_android.retrofit.dto.response.data.BrandDetailItemResponseData
import com.example.ablebody_android.retrofit.dto.response.data.BrandDetailCodyResponseData
import com.example.ablebody_android.retrofit.dto.response.data.BrandMainResponseData
import com.example.ablebody_android.retrofit.dto.response.data.FCMTokenAndAppVersionUpdateResponseData
import com.example.ablebody_android.retrofit.dto.response.data.FindCodyResponseData
import com.example.ablebody_android.retrofit.dto.response.data.FindItemResponseData
import com.example.ablebody_android.retrofit.dto.response.data.NewUserCreateResponseData
import com.example.ablebody_android.retrofit.dto.response.data.ReadBookmarkCodyData
import com.example.ablebody_android.retrofit.dto.response.data.ReadBookmarkItemData
import com.example.ablebody_android.retrofit.dto.response.data.RefreshTokenResponseData
import com.example.ablebody_android.retrofit.dto.response.data.SMSCheckResponseData
import com.example.ablebody_android.retrofit.dto.response.data.SMSSendResponseData
import com.example.ablebody_android.retrofit.dto.response.data.UserDataResponseData

/** Onboarding **/

typealias StringResponse = AbleBodyResponse<String>
typealias SendSMSResponse = AbleBodyResponse<SMSSendResponseData>
typealias CheckSMSResponse = AbleBodyResponse<SMSCheckResponseData>
typealias NewUserCreateResponse = AbleBodyResponse<NewUserCreateResponseData>
typealias RefreshTokenResponse = AbleBodyResponse<RefreshTokenResponseData>
typealias UserDataResponse = AbleBodyResponse<UserDataResponseData>
typealias FCMTokenAndAppVersionUpdateResponse = AbleBodyResponse<FCMTokenAndAppVersionUpdateResponseData>

/** Brand **/

typealias BrandMainResponse = AbleBodyResponse<List<BrandMainResponseData>>
typealias BrandDetailItemResponse = AbleBodyResponse<BrandDetailItemResponseData>
typealias BrandDetailCodyResponse = AbleBodyResponse<BrandDetailCodyResponseData>

/** Bookmark **/

typealias AddBookmarkItemResponse = AbleBodyResponse<String>
typealias ReadBookmarkItemResponse = AbleBodyResponse<ReadBookmarkItemData>
typealias DeleteBookmarkItemResponse = AbleBodyResponse<String>
typealias AddBookmarkCodyResponse = AbleBodyResponse<String>
typealias ReadBookmarkCodyResponse = AbleBodyResponse<ReadBookmarkCodyData>
typealias DeleteBookmarkCodyResponse = AbleBodyResponse<String>

/** Find **/

typealias FindItemResponse = AbleBodyResponse<FindItemResponseData>
typealias FindCodyResponse = AbleBodyResponse<FindCodyResponseData>