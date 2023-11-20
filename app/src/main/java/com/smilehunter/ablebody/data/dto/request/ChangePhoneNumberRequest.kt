package com.smilehunter.ablebody.data.dto.request

data class ChangePhoneNumberRequest(
    val phoneConfirmId: Long,
    val verifyingCode: String
)
