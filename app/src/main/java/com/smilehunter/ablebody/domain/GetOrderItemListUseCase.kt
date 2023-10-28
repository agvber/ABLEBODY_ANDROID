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

private fun GetOrderListResponseData.toDomain() = OrderItemData(
    id = id,
    itemName = itemName,
    itemImageURL = itemImage,
    amountOfPayment = amountOfPayment,
    brandName = brand,
    itemOptionDetailList = itemOptionDetailList.map {
        OrderItemData.ItemOptionDetail(
            id = it.id,
            it.orderList,
            it.itemOption,
            it.itemOptionDetail
        )
    },
    orderStatus = when(orderStatus) {
        GetOrderListResponseData.OrderStatus.DEPOSIT_WAITING -> DEPOSIT_WAITING
        GetOrderListResponseData.OrderStatus.DEPOSIT_COMPLETED -> DEPOSIT_COMPLETED
        GetOrderListResponseData.OrderStatus.ON_DELIVERY -> ON_DELIVERY
        GetOrderListResponseData.OrderStatus.DELIVERY_COMPLETED -> DELIVERY_COMPLETED
        GetOrderListResponseData.OrderStatus.ORDER_CANCELED -> ORDER_CANCELED
        GetOrderListResponseData.OrderStatus.REFUND_REQUEST -> REFUND_REQUEST
        GetOrderListResponseData.OrderStatus.REFUND_COMPLETED -> REFUND_COMPLETED
        GetOrderListResponseData.OrderStatus.EXCHANGE_REQUEST -> EXCHANGE_REQUEST
        GetOrderListResponseData.OrderStatus.ON_EXCHANGE_DELIVERY -> ON_EXCHANGE_DELIVERY
        GetOrderListResponseData.OrderStatus.EXCHANGE_COMPLETED -> EXCHANGE_COMPLETED
    },
    orderedDate = LocalDateTime.parse(orderedDate, DateTimeFormatter.ISO_ZONED_DATE_TIME).let {
        "${it.year}.${it.monthValue}.${it.dayOfMonth}"
    }
)