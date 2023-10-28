package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.UserInfoPreferences
import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.response.GetCouponBagsResponse
import com.smilehunter.ablebody.data.dto.response.UserDataResponse
import com.smilehunter.ablebody.datastore.DataStoreService
import com.smilehunter.ablebody.model.LocalUserInfoData
import com.smilehunter.ablebody.network.NetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val dataStoreService: DataStoreService
): UserRepository {

    override suspend fun getMyUserData(): UserDataResponse = networkService.getMyUserData()

    override suspend fun getUserData(uid: String): UserDataResponse = networkService.getUserData(uid)

    override val localUserInfoData: Flow<LocalUserInfoData>
        get() = dataStoreService.getUserInfoData()
            .onEach { userInfoDataStore ->
                if (userInfoDataStore.uid.isEmpty()) {
                    getMyUserData().data?.apply {
                        updateLocalUserInfo {
                            it.copy(
                                uid = uid,
                                name = name,
                                nickname = nickname,
                                gender = when (gender) {
                                    Gender.MALE -> LocalUserInfoData.Gender.MALE
                                    Gender.FEMALE -> LocalUserInfoData.Gender.FEMALE
                                },
                                profileImageUrl = profileUrl
                            )
                        }
                    }
                }
            }
            .map { userInfoDataStore ->
                userInfoDataStore.asExternalModel()
            }

    override suspend fun updateLocalUserInfo(localUserInfoData: (LocalUserInfoData) -> LocalUserInfoData) {
        dataStoreService.updateUserInfoData {
            val data = localUserInfoData(it.asExternalModel())
            it.toBuilder()
                .setUid(data.uid)
                .setName(data.name)
                .setNickname(data.nickname)
                .setGender(
                    when(data.gender) {
                        LocalUserInfoData.Gender.MALE -> com.smilehunter.ablebody.Gender.MALE
                        LocalUserInfoData.Gender.FEMALE -> com.smilehunter.ablebody.Gender.FEMALE
                        LocalUserInfoData.Gender.UNRECOGNIZED -> com.smilehunter.ablebody.Gender.UNRECOGNIZED
                    }
                )
                .setProfileImageUrl(data.profileImageUrl)
                .build()
        }
    }

    override suspend fun getCouponBags(): GetCouponBagsResponse = networkService.getCouponBags()

}

private fun UserInfoPreferences.asExternalModel() =
    LocalUserInfoData(
        uid = uid,
        name = name,
        nickname = nickname,
        gender = when (gender) {
            com.smilehunter.ablebody.Gender.MALE -> LocalUserInfoData.Gender.MALE
            com.smilehunter.ablebody.Gender.FEMALE -> LocalUserInfoData.Gender.FEMALE
            else -> LocalUserInfoData.Gender.UNRECOGNIZED
        },
        profileImageUrl = profileImageUrl
    )