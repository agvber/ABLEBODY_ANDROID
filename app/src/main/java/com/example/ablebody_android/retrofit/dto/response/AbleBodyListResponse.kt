package com.example.ablebody_android.retrofit.dto.response

data class AbleBodyListResponse<out T>(
    val code: Int,
    val message: String,
    val success: Boolean,
    val dataList: List<T>?
)