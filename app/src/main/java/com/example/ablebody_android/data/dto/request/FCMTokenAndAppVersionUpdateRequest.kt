package com.example.ablebody_android.data.dto.request

import com.google.gson.annotations.SerializedName

data class FCMTokenAndAppVersionUpdateRequest(
    val fcmToken: String,
    @SerializedName("version") val appVersion: String
)
