package com.smilehunter.ablebody.presentation.payment.data

data class PaymentPassthroughData(
    val itemID: Int,
    val brandName: String,
    val itemName: String,
    val price: Int,
    val salePrice: Int,
    val salePercentage: Int,
    val deliveryPrice: Int,
    val itemImageURL: String,
    val itemContentOptions: List<String>,
    val itemIDOptions: List<Long>
) {
    val differencePrice = salePrice - price
}