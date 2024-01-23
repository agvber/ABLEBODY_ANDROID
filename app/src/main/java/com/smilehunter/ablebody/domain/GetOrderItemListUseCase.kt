package com.smilehunter.ablebody.domain

import com.smilehunter.ablebody.data.dto.response.data.GetOrderListResponseData
import com.smilehunter.ablebody.data.repository.OrderManagementRepository
import com.smilehunter.ablebody.model.OrderItemData
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.DELIVERY_COMPLETED
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.DEPOSIT_COMPLETED
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.DEPOSIT_WAITING
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.EXCHANGE_COMPLETED
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.EXCHANGE_REQUEST
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.ON_DELIVERY
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.ON_EXCHANGE_DELIVERY
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.ORDER_CANCELED
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.REFUND_COMPLETED
import com.smilehunter.ablebody.model.OrderItemData.OrderStatus.REFUND_REQUEST
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.IO
import com.smilehunter.ablebody.network.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetOrderItemListUseCase @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val orderManagementRepository: OrderManagementRepository
) {

    suspend operator fun invoke(): List<OrderItemData> = withContext(ioDispatcher) {
        orderManagementRepository.getOrderItems().data!!.map { it.toDomain() }
    }
}

private fun GetOrderListResponseData.toDomain(): OrderItemData {
    val itemOptionDetailList = mutableListOf<OrderItemData.ItemOptionDetail>()
    orderListItemList.forEach { orders ->
        orders.colorOption?.let { colorOption ->
            itemOptionDetailList.add(
                OrderItemData.ItemOptionDetail(
                    id = 0,
                    orderNumber = id,
                    itemOption = "색상",
                    itemOptionDetail = colorOption
                )
            )
        }
        orders.sizeOption?.let { sizeOption ->
            itemOptionDetailList.add(
                OrderItemData.ItemOptionDetail(
                    id = 0,
                    orderNumber = id,
                    itemOption = "사이즈",
                    itemOptionDetail = sizeOption
                )
            )
        }
    }

    return OrderItemData(
        id = id,
        itemName = orderListItemList.first().itemName,
        itemImageURL = orderListItemList.first().itemImage,
        amountOfPayment = amount,
        brandName = orderListItemList.first().brand,
        itemOptionDetailList = itemOptionDetailList,
        orderStatus = when(orderStatus) {
            GetOrderListResponseData.OrderStatus.WAIT_FOR_PAYMENT -> DEPOSIT_WAITING
            GetOrderListResponseData.OrderStatus.CONFIRMATION_OF_DEPOSIT -> DEPOSIT_COMPLETED
            GetOrderListResponseData.OrderStatus.DELIVERY_STARTS -> ON_DELIVERY
            GetOrderListResponseData.OrderStatus.DELIVERY_COMPLETED -> DELIVERY_COMPLETED
            GetOrderListResponseData.OrderStatus.ORDER_CANCELED -> ORDER_CANCELED
            GetOrderListResponseData.OrderStatus.REQUEST_FOR_REFUND_RECOVERY -> REFUND_REQUEST
            GetOrderListResponseData.OrderStatus.REFUND_COMPLETED -> REFUND_COMPLETED
            GetOrderListResponseData.OrderStatus.EXCHANGE_ORDER_ACCEPTED -> EXCHANGE_REQUEST
            GetOrderListResponseData.OrderStatus.EXCHANGE_PROCESSING -> ON_EXCHANGE_DELIVERY
            GetOrderListResponseData.OrderStatus.EXCHANGE_COMPLETED -> EXCHANGE_COMPLETED
            GetOrderListResponseData.OrderStatus.SHIPMENT_PROCESSING -> DEPOSIT_COMPLETED
            GetOrderListResponseData.OrderStatus.SHIPMENT_COMPLETED -> DEPOSIT_COMPLETED
            GetOrderListResponseData.OrderStatus.CONFIRMATION_OF_PURCHASE -> DELIVERY_COMPLETED
            GetOrderListResponseData.OrderStatus.PAYMENT_ERROR -> DEPOSIT_WAITING
            GetOrderListResponseData.OrderStatus.REQUEST_FOR_EXCHANGE_RECOVERY -> ON_EXCHANGE_DELIVERY
            GetOrderListResponseData.OrderStatus.EXCHANGE_RECOVERY_COMPLETED -> ON_EXCHANGE_DELIVERY
            GetOrderListResponseData.OrderStatus.EXCHANGE_DELIVERY_COMPLETED -> ON_EXCHANGE_DELIVERY
            GetOrderListResponseData.OrderStatus.EXCHANGE_CANCELED -> ON_EXCHANGE_DELIVERY
            GetOrderListResponseData.OrderStatus.REFUND_RECOVERY_COMPLETED -> REFUND_REQUEST
            GetOrderListResponseData.OrderStatus.REFUND_DELIVERY_COMPLETED -> REFUND_REQUEST
            GetOrderListResponseData.OrderStatus.REFUND_PROCESSING -> REFUND_REQUEST
        },
        orderedDate = LocalDateTime.parse(orderedDate, DateTimeFormatter.ISO_ZONED_DATE_TIME).let {
            "${it.year}.${it.monthValue}.${it.dayOfMonth}"
        }
    )
}