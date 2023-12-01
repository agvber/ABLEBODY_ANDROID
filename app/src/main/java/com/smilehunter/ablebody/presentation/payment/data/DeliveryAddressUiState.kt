package com.smilehunter.ablebody.presentation.payment.data

import com.smilehunter.ablebody.model.DeliveryAddressData

sealed interface DeliveryAddressUiState {

    object Loading: DeliveryAddressUiState

    data class LoadFail(val t: Throwable?): DeliveryAddressUiState

    data class Success(val data: DeliveryAddressData): DeliveryAddressUiState
}