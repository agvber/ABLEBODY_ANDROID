package com.smilehunter.ablebody.data.dto.response.data

data class GetAddressResponseData(
    val id: Int,
    val receiverName: String,
    val addressInfo: String,
    val detailAddress: String,
    val zipCode: String,
    val phoneNum: String,
    val deliveryRequest: String
)
