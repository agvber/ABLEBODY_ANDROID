package com.smilehunter.ablebody.data.dto.response.data

import com.google.gson.annotations.SerializedName

data class FCMTokenAndAppVersionUpdateResponseData(
    val fcmToken: String,
    @SerializedName("version") val appVersion: String
)
