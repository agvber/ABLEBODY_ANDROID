package com.smilehunter.ablebody.data.dto.response.data

data class GetOrderListResponseData(
    val id: String,
    val itemName: String,
    val itemImage: String,
    val amountOfPayment: Int,
    val brand: String,
    val itemOptionDetailList: List<ItemOptionDetail>,
    val orderStatus: OrderStatus,
    val orderedDate: String
) {
    data class ItemOptionDetail(
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
