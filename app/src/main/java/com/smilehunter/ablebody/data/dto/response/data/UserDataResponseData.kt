package com.smilehunter.ablebody.data.dto.response.data

import com.smilehunter.ablebody.data.dto.Gender

data class UserDataResponseData(
    val createDate: String,
    val modifiedDate: String,
    val gender: Gender,
    val uid: String,
    val phone: String,
    val nickname: String,
    val name: String,
    val height: Int?,
    val weight: Int?,
    val job: String?,
    val profileUrl: String,
    val introduction: String?,
    val creatorPoint: Int,
    val authorities: List<Authorities>,
) {
    data class Authorities(
        val authorityName: String
    )
}
