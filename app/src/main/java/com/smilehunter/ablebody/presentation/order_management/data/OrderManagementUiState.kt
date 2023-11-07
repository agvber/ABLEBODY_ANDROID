package com.smilehunter.ablebody.presentation.order_management.data

import com.smilehunter.ablebody.model.DeliveryTrackingData
import com.smilehunter.ablebody.model.OrderItemData

sealed interface OrderManagementUiState {

    object Loading: OrderManagementUiState

    object LoadFail: OrderManagementUiState

    data class OrderItems(val data: List<OrderItemData>): OrderManagementUiState

    data class DeliveryTracking(val data: DeliveryTrackingData): OrderManagementUiState
}