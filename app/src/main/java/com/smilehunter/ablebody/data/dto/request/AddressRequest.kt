package com.smilehunter.ablebody.data.dto.request

data class AddressRequest(
    val receiverName: String,
    val phoneNum: String,
    val addressInfo: String,
    val detailAddress: String,
    val zipCode: String,
    val deliveryRequest: String
)