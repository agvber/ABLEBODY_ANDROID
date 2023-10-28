package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.GetCouponBagsResponse
import com.smilehunter.ablebody.data.dto.response.UserDataResponse
import com.smilehunter.ablebody.model.LocalUserInfoData
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getMyUserData(): UserDataResponse

    suspend fun getUserData(uid: String): UserDataResponse

    val localUserInfoData: Flow<LocalUserInfoData>

    suspend fun updateLocalUserInfo(localUserInfoData: (LocalUserInfoData) -> LocalUserInfoData)

    suspend fun getCouponBags(): GetCouponBagsResponse


}