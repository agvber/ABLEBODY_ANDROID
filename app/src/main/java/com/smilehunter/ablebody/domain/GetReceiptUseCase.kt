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

private fun GetOrderListDetailResponseData.toDomain(): ReceiptData {
    val itemOptionDetailList = mutableListOf<ReceiptData.ItemOptionDetailList>()
    orderListItemList.forEach { orders ->
        orders.colorOption?.let { colorOption ->
            itemOptionDetailList.add(
                ReceiptData.ItemOptionDetailList(
                    id = 0,
                    orderNumber = orderListId,
                    itemOption = "색상",
                    itemOptionDetail = colorOption
                )
            )
        }
        orders.sizeOption?.let { sizeOption ->
            itemOptionDetailList.add(
                ReceiptData.ItemOptionDetailList(
                    id = 0,
                    orderNumber = orderListId,
                    itemOption = "사이즈",
                    itemOptionDetail = sizeOption
                )
            )
        }
    }
    return ReceiptData(
        orderID = orderListId,
        orderedDate = LocalDateTime.parse(orderedDate, DateTimeFormatter.ISO_DATE_TIME),
        itemName = orderListItemList.first().itemName,
        itemImageURL = orderListItemList.first().itemImage,
        orderStatus = when (orderStatus) {
            GetOrderListDetailResponseData.OrderStatus.WAIT_FOR_PAYMENT -> ReceiptData.OrderStatus.DEPOSIT_WAITING
            GetOrderListDetailResponseData.OrderStatus.CONFIRMATION_OF_DEPOSIT -> ReceiptData.OrderStatus.DEPOSIT_COMPLETED
            GetOrderListDetailResponseData.OrderStatus.DELIVERY_STARTS -> ReceiptData.OrderStatus.ON_DELIVERY
            GetOrderListDetailResponseData.OrderStatus.DELIVERY_COMPLETED -> ReceiptData.OrderStatus.DELIVERY_COMPLETED
            GetOrderListDetailResponseData.OrderStatus.ORDER_CANCELED -> ReceiptData.OrderStatus.ORDER_CANCELED
            GetOrderListDetailResponseData.OrderStatus.REQUEST_FOR_REFUND_RECOVERY -> ReceiptData.OrderStatus.REFUND_REQUEST
            GetOrderListDetailResponseData.OrderStatus.REFUND_COMPLETED -> ReceiptData.OrderStatus.REFUND_COMPLETED
            GetOrderListDetailResponseData.OrderStatus.EXCHANGE_ORDER_ACCEPTED -> ReceiptData.OrderStatus.EXCHANGE_REQUEST
            GetOrderListDetailResponseData.OrderStatus.EXCHANGE_PROCESSING -> ReceiptData.OrderStatus.ON_EXCHANGE_DELIVERY
            GetOrderListDetailResponseData.OrderStatus.EXCHANGE_COMPLETED -> ReceiptData.OrderStatus.EXCHANGE_COMPLETED
            GetOrderListDetailResponseData.OrderStatus.SHIPMENT_PROCESSING -> ReceiptData.OrderStatus.DEPOSIT_COMPLETED
            GetOrderListDetailResponseData.OrderStatus.SHIPMENT_COMPLETED -> ReceiptData.OrderStatus.DEPOSIT_COMPLETED
            GetOrderListDetailResponseData.OrderStatus.CONFIRMATION_OF_PURCHASE -> ReceiptData.OrderStatus.DELIVERY_COMPLETED
            GetOrderListDetailResponseData.OrderStatus.PAYMENT_ERROR -> ReceiptData.OrderStatus.DEPOSIT_WAITING
            GetOrderListDetailResponseData.OrderStatus.REQUEST_FOR_EXCHANGE_RECOVERY -> ReceiptData.OrderStatus.ON_EXCHANGE_DELIVERY
            GetOrderListDetailResponseData.OrderStatus.EXCHANGE_RECOVERY_COMPLETED -> ReceiptData.OrderStatus.ON_EXCHANGE_DELIVERY
            GetOrderListDetailResponseData.OrderStatus.EXCHANGE_DELIVERY_COMPLETED -> ReceiptData.OrderStatus.ON_EXCHANGE_DELIVERY
            GetOrderListDetailResponseData.OrderStatus.EXCHANGE_CANCELED -> ReceiptData.OrderStatus.ON_EXCHANGE_DELIVERY
            GetOrderListDetailResponseData.OrderStatus.REFUND_RECOVERY_COMPLETED -> ReceiptData.OrderStatus.REFUND_REQUEST
            GetOrderListDetailResponseData.OrderStatus.REFUND_DELIVERY_COMPLETED -> ReceiptData.OrderStatus.REFUND_REQUEST
            GetOrderListDetailResponseData.OrderStatus.REFUND_PROCESSING -> ReceiptData.OrderStatus.REFUND_REQUEST
        },
        brandName = orderListItemList.first().brand,
        receiverName = receiverName,
        roadAddress = addressInfo,
        roadAddressDetail = addressDetail,
        itemOptionDetailList = itemOptionDetailList,
        phoneNumber = phoneNum,
        depositDeadline = LocalDateTime.now().plusDays(365),
        price = itemPrice,
        itemDiscount = itemDiscount,
        couponDiscount = couponDiscount,
        pointDiscount = pointDiscount,
        deliveryPrice = deliveryPrice,
        amountOfPayment = amount
    )
}