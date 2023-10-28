package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.GetDeliveryInfoResponse
import com.smilehunter.ablebody.data.dto.response.GetOrderListResponse
import com.smilehunter.ablebody.network.NetworkService
import javax.inject.Inject

class OrderManagementRepositoryImpl @Inject constructor(
    private val networkService: NetworkService
): OrderManagementRepository {
    override suspend fun getOrderItems(): GetOrderListResponse {
        return networkService.getOrderList()
    }

    override suspend fun cancelOrderItem(id: String) {
        networkService.cancelOrderList(id)
    }

    override suspend fun getDeliveryTrackingNumber(id: String): GetDeliveryInfoResponse {
        return networkService.getDeliveryInfo(id)
    }


}