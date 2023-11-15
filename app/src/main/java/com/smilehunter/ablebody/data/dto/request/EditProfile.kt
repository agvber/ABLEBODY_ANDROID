package com.smilehunter.ablebody.data.dto.request

import androidx.annotation.IntRange

data class EditProfile(
    val nickname: String?,
    val name: String?,
    val height: Int?,
    val weight: Int?,
    val job: String?,
    val introduction: String?,
    @IntRange(0, 5) val defaultProfileImage: Int?
)