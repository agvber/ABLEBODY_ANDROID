package com.smilehunter.ablebody.data.dto.response

import com.google.gson.annotations.SerializedName

data class AbleBodyResponse<out T>(
    val code: Int,
    val message: String,
    val success: Boolean,
    @SerializedName(value = "data", alternate = ["dataList"]) val data: T?
)
