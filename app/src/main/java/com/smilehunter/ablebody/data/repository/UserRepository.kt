package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.GetAddressResponse
import com.smilehunter.ablebody.data.dto.response.GetCouponBagsResponse
import com.smilehunter.ablebody.data.dto.response.GetMyBoardResponse
import com.smilehunter.ablebody.data.dto.response.UserDataResponse
import com.smilehunter.ablebody.model.LocalUserInfoData
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getMyUserData(): UserDataResponse

    suspend fun getUserData(uid: String): UserDataResponse

    val localUserInfoData: Flow<LocalUserInfoData>

    suspend fun updateLocalUserInfo(localUserInfoData: (LocalUserInfoData) -> LocalUserInfoData)

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

    suspend fun getMyBoard(
        uid: String? = null,
        page: Int = 0,
        size: Int = 10
    ): GetMyBoardResponse

    suspend fun suggestApp(
        text: String
    )
}