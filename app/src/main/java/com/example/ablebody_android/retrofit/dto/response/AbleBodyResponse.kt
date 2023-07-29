package com.example.ablebody_android.retrofit.dto.response

data class AbleBodyResponse<out T>(
    val code: Int,
    val message: String,
    val success: Boolean,
    val data: T?
)
