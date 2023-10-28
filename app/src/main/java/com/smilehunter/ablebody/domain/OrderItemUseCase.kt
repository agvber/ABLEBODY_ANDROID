package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.repository.OrderManagementRepository
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.IO
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderItemUseCase @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val orderManagementRepository: OrderManagementRepository
) {

    suspend operator fun invoke(
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
        itemOptionIdList: List<Long>
    ): String = withContext(ioDispatcher) {
        orderManagementRepository.orderItem(
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
        ).data!! //order_ID
    }
}