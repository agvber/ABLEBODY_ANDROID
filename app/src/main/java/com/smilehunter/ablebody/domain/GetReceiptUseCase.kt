package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.response.data.GetOrderListDetailResponseData
import com.smilehunter.ablebody.data.repository.OrderManagementRepository
import com.smilehunter.ablebody.model.ReceiptData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.IO
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetReceiptUseCase @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val orderManagementRepository: OrderManagementRepository
) {

    suspend operator fun invoke(id: String): ReceiptData = withContext(ioDispatcher) {
        orderManagementRepository.getOrderDetailItem(id).data!!.toDomain()
    }
}

private fun GetOrderListDetailResponseData.toDomain() =
    ReceiptData(
        orderID = orderListId,
        orderedDate = LocalDateTime.parse(orderedDate, DateTimeFormatter.ISO_DATE_TIME),
        itemName = itemName,
        itemImageURL = itemImage,
        orderStatus = when (orderStatus) {
            GetOrderListDetailResponseData.OrderStatus.DEPOSIT_WAITING -> ReceiptData.OrderStatus.DEPOSIT_WAITING
            GetOrderListDetailResponseData.OrderStatus.DEPOSIT_COMPLETED -> ReceiptData.OrderStatus.DEPOSIT_COMPLETED
            GetOrderListDetailResponseData.OrderStatus.ON_DELIVERY -> ReceiptData.OrderStatus.ON_DELIVERY
            GetOrderListDetailResponseData.OrderStatus.DELIVERY_COMPLETED -> ReceiptData.OrderStatus.DELIVERY_COMPLETED
            GetOrderListDetailResponseData.OrderStatus.ORDER_CANCELED -> ReceiptData.OrderStatus.ORDER_CANCELED
            GetOrderListDetailResponseData.OrderStatus.REFUND_REQUEST -> ReceiptData.OrderStatus.REFUND_REQUEST
            GetOrderListDetailResponseData.OrderStatus.REFUND_COMPLETED -> ReceiptData.OrderStatus.REFUND_COMPLETED
            GetOrderListDetailResponseData.OrderStatus.EXCHANGE_REQUEST -> ReceiptData.OrderStatus.EXCHANGE_REQUEST
            GetOrderListDetailResponseData.OrderStatus.ON_EXCHANGE_DELIVERY -> ReceiptData.OrderStatus.ON_EXCHANGE_DELIVERY
            GetOrderListDetailResponseData.OrderStatus.EXCHANGE_COMPLETED -> ReceiptData.OrderStatus.EXCHANGE_COMPLETED
        },
        brandName = brand,
        receiverName = receiverName,
        roadAddress = addressInfo,
        roadAddressDetail = addressDetail,
        itemOptionDetailList = itemOptionDetailList.map {
            ReceiptData.ItemOptionDetailList(
                id = it.id,
                orderNumber = it.orderList,
                itemOption = it.itemOption,
                itemOptionDetail = it.itemOptionDetail
            )
        },
        phoneNumber = phoneNum,
        depositDeadline = LocalDateTime.parse(depositDeadline, DateTimeFormatter.ISO_DATE_TIME),
        price = price,
        itemDiscount = itemDiscount,
        couponDiscount = couponDiscount,
        pointDiscount = pointDiscount,
        deliveryPrice = deliveryPrice,
        amountOfPayment = amountOfPayment
    )