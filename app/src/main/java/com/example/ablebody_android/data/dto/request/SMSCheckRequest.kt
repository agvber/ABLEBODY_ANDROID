package com.example.ablebody_android.data.dto.request

data class SMSCheckRequest(
    val phoneConfirmId: Long,
    val verifyingCode: String
)
