package com.smilehunter.ablebody.data.dto.response.data

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponseData(
    @SerializedName("auth_token") val authToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
)
