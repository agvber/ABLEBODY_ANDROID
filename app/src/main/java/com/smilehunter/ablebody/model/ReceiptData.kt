package com.smilehunter.ablebody.model

import java.time.LocalDateTime

data class ReceiptData(
    val orderID: String,
    val orderedDate: LocalDateTime,
    val itemName: String,
    val itemImageURL: String,
    val orderStatus: OrderStatus,
    val brandName: String,
    val receiverName: String,
    val roadAddress: String,
    val roadAddressDetail: String,
    val itemOptionDetailList: List<ItemOptionDetailList>,
    val phoneNumber: String,
    val depositDeadline: LocalDateTime,
    val price: Int,
    val itemDiscount: Int,
    val couponDiscount: Int,
    val pointDiscount: Int,
    val deliveryPrice: Int,
    val amountOfPayment: Int
) {
    data class ItemOptionDetailList(
        val id: Int,
        val orderNumber: String,
        val itemOption: String,
        val itemOptionDetail: String
    )

    enum class OrderStatus {
        DEPOSIT_WAITING,
        DEPOSIT_COMPLETED,
        ON_DELIVERY,
        DELIVERY_COMPLETED,
        ORDER_CANCELED,
        REFUND_REQUEST,
        REFUND_COMPLETED,
        EXCHANGE_REQUEST,
        ON_EXCHANGE_DELIVERY,
        EXCHANGE_COMPLETED
    }
}
