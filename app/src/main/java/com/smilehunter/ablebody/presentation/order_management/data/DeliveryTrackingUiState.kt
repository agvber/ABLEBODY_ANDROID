package com.smilehunter.ablebody.presentation.order_management.data

import com.smilehunter.ablebody.model.DeliveryTrackingData

sealed interface DeliveryTrackingUiState {

    object Loading: DeliveryTrackingUiState

    data class LoadFail(val t: Throwable?): DeliveryTrackingUiState

    data class Success(val data: DeliveryTrackingData): DeliveryTrackingUiState
}