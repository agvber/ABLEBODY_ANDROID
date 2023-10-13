package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.UserDataResponse

interface UserRepository {

    suspend fun getMyUserData(): UserDataResponse
    suspend fun getUserData(uid: String): UserDataResponse
}