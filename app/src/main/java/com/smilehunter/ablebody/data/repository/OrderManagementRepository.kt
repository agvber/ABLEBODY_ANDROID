package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.AddOrderListResponse
import com.smilehunter.ablebody.data.dto.response.GetDeliveryInfoResponse
import com.smilehunter.ablebody.data.dto.response.GetOrderListResponse

interface OrderManagementRepository {

    suspend fun getOrderItems(): GetOrderListResponse

    suspend fun cancelOrderItem(id: String)

    suspend fun getDeliveryTrackingNumber(id: String): GetDeliveryInfoResponse

    suspend fun orderItem(
        itemID: Int,
        addressID: Int,
        couponBagsID: Int?,
        refundBankName: String,
        refundAccount: String,
        refundAccountHolder: String,
        paymentMethod: String,
        price: Int,
        itemDiscount: Int,
        couponDiscount: Int,
        pointDiscount: Int,
        deliveryPrice: Int,
        amountOfPayment: Int,
        itemOptionIdList: List<Long>?
    ): AddOrderListResponse
}