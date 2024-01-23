package com.smilehunter.ablebody.data.dto.request

import com.google.gson.annotations.SerializedName

data class SMSSendRequest(
    @SerializedName("phone") val phoneNumber: String,
    @SerializedName("sms") val isNotTestMessage: Boolean
)
