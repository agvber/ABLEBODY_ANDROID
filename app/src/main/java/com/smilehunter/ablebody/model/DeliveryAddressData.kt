package com.smilehunter.ablebody.model

data class DeliveryAddressData(
    val id: Int,
    val userName: String,
    val roadAddress: String,
    val roadDetailAddress: String,
    val zipCode: String,
    val phoneNumber: String,
    val deliveryRequestMessage: String
)
