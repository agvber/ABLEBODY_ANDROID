package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.response.data.UserDataResponseData
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.model.UserInfoData
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(): UserInfoData =
        userRepository.getMyUserData().data!!.toDomain()


    suspend operator fun invoke(uid: String): UserInfoData =
        userRepository.getUserData(uid).data!!.toDomain()

}

private fun UserDataResponseData.toDomain() =
    UserInfoData(
        createDate = createDate,
        modifiedDate = modifiedDate,
        gender = gender,
        uid = uid,
        phoneNumber = phone,
        nickname = nickname,
        name = name,
        height = height,
        weight = weight,
        job = job,
        profileUrl = profileUrl,
        introduction = introduction,
        creatorPoint = creatorPoint,
        authorities = authorities.map { UserInfoData.Authorities(it.authorityName) },
    )