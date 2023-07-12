package com.example.ablebody_android.retrofit.dto.request

import com.google.gson.annotations.SerializedName

data class TokenRefreshRequest(
    @SerializedName("refresh_token") val refreshToken: String
)
