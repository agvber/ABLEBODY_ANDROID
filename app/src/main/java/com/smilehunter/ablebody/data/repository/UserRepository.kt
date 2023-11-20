package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.request.ChangePhoneNumberRequest
import com.smilehunter.ablebody.data.dto.request.EditProfile
import com.smilehunter.ablebody.data.dto.response.GetAddressResponse
import com.smilehunter.ablebody.data.dto.response.GetCouponBagsResponse
import com.smilehunter.ablebody.data.dto.response.GetMyBoardResponse
import com.smilehunter.ablebody.data.dto.response.UserDataResponse
import com.smilehunter.ablebody.model.LocalUserInfoData
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface UserRepository {

    suspend fun getMyUserData(): UserDataResponse

    suspend fun getUserData(uid: String): UserDataResponse

    val localUserInfoData: Flow<LocalUserInfoData>

    suspend fun updateLocalUserInfo(localUserInfoData: (LocalUserInfoData) -> LocalUserInfoData)

    suspend fun editProfile(
        editProfile: EditProfile,
        profileImageInputStream: InputStream?
    ): UserDataResponse

    suspend fun getMyBoard(
        uid: String? = null,
        page: Int = 0,
        size: Int = 10
    ): GetMyBoardResponse

    suspend fun getCouponBags(): GetCouponBagsResponse

    suspend fun getMyAddress(): GetAddressResponse

    suspend fun addMyAddress(
        name: String,
        phoneNumber: String,
        roadAddress: String,
        roadDetailAddress: String,
        zipCode: String,
        deliveryRequestMessage: String
    )

    suspend fun editMyAddress(
        name: String,
        phoneNumber: String,
        roadAddress: String,
        roadDetailAddress: String,
        zipCode: String,
        deliveryRequestMessage: String
    )

    suspend fun getUserAdConsent(): Boolean

    suspend fun acceptUserAdConsent(): String

    suspend fun changePhoneNumber(changePhoneNumberRequest: ChangePhoneNumberRequest): UserDataResponse

    suspend fun resignUser(reason: String): String

    suspend fun suggestApp(text: String)
}