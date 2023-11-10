package com.smilehunter.ablebody.network

import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.HomeCategory
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.dto.response.AbleBodyResponse
import com.smilehunter.ablebody.data.dto.response.AcceptUserAdConsentResponse
import com.smilehunter.ablebody.data.dto.response.AddAddressResponse
import com.smilehunter.ablebody.data.dto.response.AddBookmarkCodyResponse
import com.smilehunter.ablebody.data.dto.response.AddBookmarkItemResponse
import com.smilehunter.ablebody.data.dto.response.AddOrderListResponse
import com.smilehunter.ablebody.data.dto.response.BrandDetailCodyResponse
import com.smilehunter.ablebody.data.dto.response.BrandDetailItemResponse
import com.smilehunter.ablebody.data.dto.response.BrandMainResponse
import com.smilehunter.ablebody.data.dto.response.CancelOrderListResponse
import com.smilehunter.ablebody.data.dto.response.CheckMyNotiResponse
import com.smilehunter.ablebody.data.dto.response.CheckSMSResponse
import com.smilehunter.ablebody.data.dto.response.CreatorDetailCommentResponse
import com.smilehunter.ablebody.data.dto.response.CreatorDetailDeleteCommentReplyResponse
import com.smilehunter.ablebody.data.dto.response.CreatorDetailDeleteResponse
import com.smilehunter.ablebody.data.dto.response.CreatorDetailLikeResponse
import com.smilehunter.ablebody.data.dto.response.CreatorDetailLikeUsersResponse
import com.smilehunter.ablebody.data.dto.response.CreatorDetailReplyResponse
import com.smilehunter.ablebody.data.dto.response.CreatorDetailResponse
import com.smilehunter.ablebody.data.dto.response.DeleteBookmarkCodyResponse
import com.smilehunter.ablebody.data.dto.response.DeleteBookmarkItemResponse
import com.smilehunter.ablebody.data.dto.response.EditAddressResponse
import com.smilehunter.ablebody.data.dto.response.FCMTokenAndAppVersionUpdateResponse
import com.smilehunter.ablebody.data.dto.response.FindCodyResponse
import com.smilehunter.ablebody.data.dto.response.FindItemResponse
import com.smilehunter.ablebody.data.dto.response.GetAddressResponse
import com.smilehunter.ablebody.data.dto.response.GetCouponBagsResponse
import com.smilehunter.ablebody.data.dto.response.GetDeliveryInfoResponse
import com.smilehunter.ablebody.data.dto.response.GetMyBoardResponse
import com.smilehunter.ablebody.data.dto.response.GetMyNotiResponse
import com.smilehunter.ablebody.data.dto.response.GetOrderListDetailResponse
import com.smilehunter.ablebody.data.dto.response.GetOrderListResponse
import com.smilehunter.ablebody.data.dto.response.GetUserAdConsentResponse
import com.smilehunter.ablebody.data.dto.response.ItemDetailResponse
import com.smilehunter.ablebody.data.dto.response.NewUserCreateResponse
import com.smilehunter.ablebody.data.dto.response.RefreshTokenResponse
import com.smilehunter.ablebody.data.dto.response.SearchCodyResponse
import com.smilehunter.ablebody.data.dto.response.SearchItemResponse
import com.smilehunter.ablebody.data.dto.response.SendSMSResponse
import com.smilehunter.ablebody.data.dto.response.StringResponse
import com.smilehunter.ablebody.data.dto.response.SuggestionResponse
import com.smilehunter.ablebody.data.dto.response.UniSearchResponse
import com.smilehunter.ablebody.data.dto.response.UserDataResponse
import com.smilehunter.ablebody.data.dto.response.data.ReadBookmarkCodyData
import com.smilehunter.ablebody.data.dto.response.data.ReadBookmarkItemData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

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

    suspend fun creatorDetail(
        id: Long
    ): CreatorDetailResponse

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

    suspend fun creatorDetailDelete(
        id: Long
    ): CreatorDetailDeleteResponse

    /** item **/

    suspend fun itemDetail(
        id: Long
    ): ItemDetailResponse

    /** address **/

    suspend fun addAddress(
        receiverName: String,
        phoneNum: String,
        addressInfo: String,
        detailAddress: String,
        zipCode: String,
        deliveryRequest: String
    ): AddAddressResponse

    suspend fun getAddress(): GetAddressResponse

    suspend fun editAddress(
        receiverName: String,
        phoneNum: String,
        addressInfo: String,
        detailAddress: String,
        zipCode: String,
        deliveryRequest: String
    ): EditAddressResponse

    /** coupon **/

    suspend fun getCouponBags(): GetCouponBagsResponse

    /** order **/

    suspend fun addOrderList(
        itemID: Int,
        addressID: Int,
        couponBagsID: Int?,
        refundBankName: String,
        refundAccount: String,
        refundAccountHolder: String,
        paymentMethod: String,
        price: Int,
        itemDiscount: Int,
        couponDiscount: Int,
        pointDiscount: Int,
        deliveryPrice: Int,
        amountOfPayment: Int,
        itemOptionIdList: List<Long>?
    ): AddOrderListResponse

    suspend fun getOrderList(): GetOrderListResponse

    suspend fun cancelOrderList(
        id: String
    ): CancelOrderListResponse

    suspend fun getDeliveryInfo(
        id: String
    ): GetDeliveryInfoResponse

    suspend fun getOrderListDetail(
        id: String
    ): GetOrderListDetailResponse

    /** User **/
    suspend fun getMyUserData(): UserDataResponse

    suspend fun getUserData(uid: String): UserDataResponse

    suspend fun getMyBoard(
        uid: String? = null,
        page: Int = 0,
        size: Int = 10
    ): GetMyBoardResponse

    suspend fun suggestion(
        content: String
    ): SuggestionResponse

    /** Agreement **/

    @GET("/api/my/consent-pushad")
    suspend fun getUserAdConsent(): GetUserAdConsentResponse

    @POST("/api/my/consent-pushad")
    suspend fun acceptUserAdConsent(): AcceptUserAdConsentResponse
}