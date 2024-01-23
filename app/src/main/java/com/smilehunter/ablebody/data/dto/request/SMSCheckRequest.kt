package com.smilehunter.ablebody.data.dto.request

data class SMSCheckRequest(
    val phoneConfirmId: Long,
    val verifyingCode: String
)
