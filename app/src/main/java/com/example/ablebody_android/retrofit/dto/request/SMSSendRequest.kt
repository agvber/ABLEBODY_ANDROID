package com.example.ablebody_android.retrofit.dto.request

import com.google.gson.annotations.SerializedName

data class SMSSendRequest(
    @SerializedName("phone") val phoneNumber: String,
    @SerializedName("sms") val isNotTestMessage: Boolean
)