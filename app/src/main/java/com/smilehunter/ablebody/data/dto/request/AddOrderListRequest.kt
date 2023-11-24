package com.smilehunter.ablebody.data.dto.request

data class AddOrderListRequest(
    val addressId: Int,
    val orderName: String,
    val paymentType: String,
    val paymentMethod: String,
    val easyPayType: String?,
    val pointDiscount: Int?,
    val deliveryPrice: Int?,
    val orderListItemReqDtoList: List<OrderListItemReqDto>
) {
    data class OrderListItemReqDto(
        val itemId: Int,
        val couponBagsId: Int?,
        val colorOption: String?,
        val sizeOption: String?,
        val itemPrice: Int,
        val itemCount: Int,
        val itemDiscount: Int,
        val couponDiscount: Int,
        val amount: Int
    )
}
