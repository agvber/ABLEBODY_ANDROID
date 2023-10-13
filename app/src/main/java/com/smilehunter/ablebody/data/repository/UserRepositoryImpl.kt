package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.UserDataResponse
import com.smilehunter.ablebody.network.NetworkService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): UserRepository {
    override suspend fun getMyUserData(): UserDataResponse =
        networkService.getMyUserData()


    override suspend fun getUserData(uid: String): UserDataResponse =
        networkService.getUserData(uid)

}