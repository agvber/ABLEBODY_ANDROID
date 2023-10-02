package com.smilehunter.ablebody.data.dto.response

import com.smilehunter.ablebody.data.dto.response.data.BrandDetailItemResponseData
import com.smilehunter.ablebody.data.dto.response.data.BrandDetailCodyResponseData
import com.smilehunter.ablebody.data.dto.response.data.BrandMainResponseData
import com.smilehunter.ablebody.data.dto.response.data.CreatorDetailCommentResponseData
import com.smilehunter.ablebody.data.dto.response.data.CreatorDetailLikeUsersResponseData
import com.smilehunter.ablebody.data.dto.response.data.CreatorDetailReplyResponseData
import com.smilehunter.ablebody.data.dto.response.data.FCMTokenAndAppVersionUpdateResponseData
import com.smilehunter.ablebody.data.dto.response.data.FindCodyResponseData
import com.smilehunter.ablebody.data.dto.response.data.FindItemResponseData
import com.smilehunter.ablebody.data.dto.response.data.GetMyNotiResponseData
import com.smilehunter.ablebody.data.dto.response.data.NewUserCreateResponseData
import com.smilehunter.ablebody.data.dto.response.data.ReadBookmarkCodyData
import com.smilehunter.ablebody.data.dto.response.data.ReadBookmarkItemData
import com.smilehunter.ablebody.data.dto.response.data.RefreshTokenResponseData
import com.smilehunter.ablebody.data.dto.response.data.SMSCheckResponseData
import com.smilehunter.ablebody.data.dto.response.data.SMSSendResponseData
import com.smilehunter.ablebody.data.dto.response.data.SearchCodyResponseData
import com.smilehunter.ablebody.data.dto.response.data.SearchItemResponseData
import com.smilehunter.ablebody.data.dto.response.data.UniSearchResponseData
import com.smilehunter.ablebody.data.dto.response.data.UserDataResponseData

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

/** Search **/

typealias UniSearchResponse = AbleBodyResponse<UniSearchResponseData>
typealias SearchItemResponse = AbleBodyResponse<SearchItemResponseData>
typealias SearchCodyResponse = AbleBodyResponse<SearchCodyResponseData>

/** Notification **/

typealias GetMyNotiResponse = AbleBodyResponse<GetMyNotiResponseData>
typealias CheckMyNotiResponse = AbleBodyResponse<String>

/** Creator **/

typealias CreatorDetailLikeResponse = AbleBodyResponse<Long>
typealias CreatorDetailLikeUsersResponse = AbleBodyResponse<List<CreatorDetailLikeUsersResponseData>>
typealias CreatorDetailCommentResponse = AbleBodyResponse<CreatorDetailCommentResponseData>
typealias CreatorDetailReplyResponse = AbleBodyResponse<CreatorDetailReplyResponseData>
typealias CreatorDetailDeleteCommentReplyResponse = AbleBodyResponse<Long>