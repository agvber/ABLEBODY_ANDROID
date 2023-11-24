package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.request.AddOrderListRequest
import com.smilehunter.ablebody.data.dto.response.AddOrderListResponse
import com.smilehunter.ablebody.data.dto.response.GetDeliveryInfoResponse
import com.smilehunter.ablebody.data.dto.response.GetOrderListDetailResponse
import com.smilehunter.ablebody.data.dto.response.GetOrderListResponse
import com.smilehunter.ablebody.data.dto.response.TossPaymentFailResponse
import com.smilehunter.ablebody.data.dto.response.TossPaymentSuccessResponse
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

    override suspend fun orderItem(
        addOrderListRequest: AddOrderListRequest
    ): AddOrderListResponse {
        return networkService.addOrderList(addOrderListRequest)
    }

    override suspend fun getOrderDetailItem(id: String): GetOrderListDetailResponse {
        return networkService.getOrderListDetail(id)
    }

    override suspend fun confirmPayment(paymentKey: String, orderListId: String, amount: String): TossPaymentSuccessResponse {
        return networkService.tossPaymentSuccess(
            paymentKey = paymentKey,
            orderListId = orderListId,
            amount = amount
        )
    }

    override suspend fun handlePaymentFailure(
        code: String,
        message: String,
        orderListId: String,
    ): TossPaymentFailResponse {
        return networkService.tossPaymentFail(
            code = code,
            message = message,
            orderListId = orderListId
        )
    }

}