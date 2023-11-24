package com.smilehunter.ablebody.data.dto.response.data

data class GetOrderListResponseData(
    val id: String,
    val orderedDate: String,
    val orderStatus: OrderStatus,
    val amount: Int,
    val orderListItemList: List<OrderItem>
) {
    data class OrderItem(
        val itemName: String,
        val itemImage: String,
        val itemCount: Int,
        val amount: Int,
        val brand: String,
        val colorOption: String?,
        val sizeOption: String?
    )

    enum class OrderStatus {
        WAIT_FOR_PAYMENT,
        CONFIRMATION_OF_DEPOSIT,
        SHIPMENT_PROCESSING,
        SHIPMENT_COMPLETED,
        DELIVERY_STARTS,
        DELIVERY_COMPLETED,
        CONFIRMATION_OF_PURCHASE,
        PAYMENT_ERROR,
        ORDER_CANCELED,
        EXCHANGE_ORDER_ACCEPTED,
        REQUEST_FOR_EXCHANGE_RECOVERY,
        EXCHANGE_RECOVERY_COMPLETED,
        EXCHANGE_DELIVERY_COMPLETED,
        EXCHANGE_PROCESSING,
        EXCHANGE_COMPLETED,
        EXCHANGE_CANCELED,
        REQUEST_FOR_REFUND_RECOVERY,
        REFUND_RECOVERY_COMPLETED,
        REFUND_DELIVERY_COMPLETED,
        REFUND_PROCESSING,
        REFUND_COMPLETED
    }
}
