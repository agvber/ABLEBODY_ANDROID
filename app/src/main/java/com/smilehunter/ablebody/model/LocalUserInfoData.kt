package com.smilehunter.ablebody.model

data class LocalUserInfoData(
    val uid: String,
    val name: String,
    val nickname: String,
    val gender: Gender,
    val profileImageUrl: String,
) {
    enum class Gender {
        MALE, FEMALE, UNRECOGNIZED
    }
}