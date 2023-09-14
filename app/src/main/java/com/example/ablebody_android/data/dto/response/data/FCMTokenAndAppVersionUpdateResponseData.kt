package com.example.ablebody_android.data.dto.response.data

import com.google.gson.annotations.SerializedName

data class FCMTokenAndAppVersionUpdateResponseData(
    val fcmToken: String,
    @SerializedName("version") val appVersion: String
)