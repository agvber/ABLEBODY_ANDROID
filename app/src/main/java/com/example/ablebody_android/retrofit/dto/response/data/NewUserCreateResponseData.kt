package com.example.ablebody_android.retrofit.dto.response.data

import com.google.gson.annotations.SerializedName

data class NewUserCreateResponseData(
    val tokens: Tokens,
    val registered: Boolean
) {
    data class Tokens(
        @SerializedName("auth_token") val authToken: String,
        @SerializedName("refresh_token") val refreshToken: String
    )
}
