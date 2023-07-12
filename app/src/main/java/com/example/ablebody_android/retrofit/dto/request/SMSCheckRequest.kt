package com.example.ablebody_android.retrofit.dto.request

data class SMSCheckRequest(
    val phoneConfirmId: Long,
    val verifyingCode: String
)
