package com.example.ablebody_android.retrofit.dto.request

data class NewUserCreateRequest(
    val gender: String,
    val nickname: String,
    val profileImage: Int,
    val verifyingCode: String,
    val agreeRequiredConsent: Boolean,
    val agreeMarketingConsent: Boolean
)
