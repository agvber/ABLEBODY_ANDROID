package com.example.ablebody_android.retrofit.dto.response

data class Response<out T>(
    val code: Int,
    val message: String,
    val success: Boolean,
    val data: T?
)
