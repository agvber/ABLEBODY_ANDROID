package com.smilehunter.ablebody.data.dto.request

data class AddOrderListRequest(
    val itemId: Int,
    val addressId: Int,
    val couponBagsId: Int?,
    val refundBankName: String,
    val refundAccount: String,
    val refundAccountHolder: String,
    val paymentMethod: String,
    val price: Int,
    val itemDiscount: Int,
    val couponDiscount: Int,
    val pointDiscount: Int,
    val deliveryPrice: Int,
    val amountOfPayment: Int,
    val itemOptionIdList: List<Long>?
)
