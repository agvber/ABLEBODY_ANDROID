package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.GetDeliveryInfoResponse
import com.smilehunter.ablebody.data.dto.response.GetOrderListResponse

interface OrderManagementRepository {

    suspend fun getOrderItems(): GetOrderListResponse

    suspend fun cancelOrderItem(id: String)

    suspend fun getDeliveryTrackingNumber(id: String): GetDeliveryInfoResponse
}