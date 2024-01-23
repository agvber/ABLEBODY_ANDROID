package com.smilehunter.ablebody.data.dto.response

import com.google.gson.annotations.SerializedName

data class AbleBodyResponse<out T>(
    val errorCode: ErrorCode?,
    val code: Int?,
    val message: String?,
    @SerializedName(value = "success", alternate = ["is_success"]) val success: Boolean,
    @SerializedName(value = "data", alternate = ["dataList"]) val data: T?
) {
    enum class ErrorCode {
        NULL_TOKEN,
        INVALID_TOKEN,
        EXPIRED_TOKEN,
        MEMBER_NOT_FOUND,
        MEMBER_NOT_ALLOWED,
        INVALID_MEMBERINFO,
        DUPLICATE_EMAIL,
        DUPLICATE_NICKNAME,
        INVALID_ITEM,
        INVALID_ADDRESS,
        UNABLE_COUPON,
        EXIST_ADDRESS,
        NON_EXIST_ADDRESS,
        INVALID_OPTION,
        INVALID_COUPON_CODE,
        ALREADY_EXIST_COUPON
    }
}