package com.smilehunter.ablebody.data.dto.response.data

data class GetOrderListDetailResponseData(
    val orderListId: String,
    val orderedDate: String,
    val itemName: String,
    val itemImage: String,
    val orderStatus: OrderStatus,
    val brand: String,
    val receiverName: String,
    val addressInfo: String,
    val addressDetail: String,
    val itemOptionDetailList: List<ItemOptionDetailList>,
    val phoneNum: String,
    val depositDeadline: String,
    val price: Int,
    val itemDiscount: Int,
    val couponDiscount: Int,
    val pointDiscount: Int,
    val deliveryPrice: Int,
    val amountOfPayment: Int
) {
    data class ItemOptionDetailList(
        val id: Int,
        val orderList: String,
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
