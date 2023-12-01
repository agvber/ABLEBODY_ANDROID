package com.smilehunter.ablebody.presentation.order_management.data

import com.smilehunter.ablebody.model.OrderItemData

sealed interface OrderItemUiState {

    object Loading: OrderItemUiState

    data class LoadFail(val t: Throwable?): OrderItemUiState

    data class Success(val data: List<OrderItemData>): OrderItemUiState
}