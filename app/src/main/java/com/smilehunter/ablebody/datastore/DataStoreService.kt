package com.smilehunter.ablebody.datastore

import com.smilehunter.ablebody.UserInfoPreferences
import kotlinx.coroutines.flow.Flow

interface DataStoreService {

    fun getUserInfoData(): Flow<UserInfoPreferences>
    suspend fun updateUserInfoData(userInfo: (UserInfoPreferences) -> UserInfoPreferences)
}