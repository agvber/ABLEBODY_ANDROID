package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.request.AddOrderListRequest
import com.smilehunter.ablebody.data.dto.response.AddOrderListResponse
import com.smilehunter.ablebody.data.dto.response.GetDeliveryInfoResponse
import com.smilehunter.ablebody.data.dto.response.GetOrderListDetailResponse
import com.smilehunter.ablebody.data.dto.response.GetOrderListResponse

interface OrderManagementRepository {

    suspend fun getOrderItems(): GetOrderListResponse

    suspend fun cancelOrderItem(id: String)

    suspend fun getDeliveryTrackingNumber(id: String): GetDeliveryInfoResponse

    suspend fun orderItem(
        addOrderListRequest: AddOrderListRequest
    ): AddOrderListResponse

    suspend fun getOrderDetailItem(
        id: String
    ): GetOrderListDetailResponse
}