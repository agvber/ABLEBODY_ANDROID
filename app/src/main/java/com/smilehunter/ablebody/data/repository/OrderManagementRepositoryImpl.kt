package com.smilehunter.ablebody.data.repository

import com.smilehunter.ablebody.data.dto.response.AddOrderListResponse
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

    override suspend fun orderItem(
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
    ): AddOrderListResponse {
        return networkService.addOrderList(
            itemID = itemID,
            addressID = addressID,
            couponBagsID = couponBagsID,
            refundBankName = refundBankName,
            refundAccount = refundAccount,
            refundAccountHolder = refundAccountHolder,
            paymentMethod = paymentMethod,
            price = price,
            itemDiscount = itemDiscount,
            couponDiscount = couponDiscount,
            pointDiscount = pointDiscount,
            deliveryPrice = deliveryPrice,
            amountOfPayment = amountOfPayment,
            itemOptionIdList = itemOptionIdList
        )
    }

}