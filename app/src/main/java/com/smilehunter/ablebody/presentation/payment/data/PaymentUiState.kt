package com.smilehunter.ablebody.presentation.payment.data

import com.smilehunter.ablebody.model.CouponData
import com.smilehunter.ablebody.model.DeliveryAddressData
import com.smilehunter.ablebody.model.UserInfoData

sealed interface PaymentUiState {

    object Loading: PaymentUiState

    object LoadFail: PaymentUiState

    data class User(val data: UserInfoData): PaymentUiState

    data class Coupons(val data: List<CouponData>): PaymentUiState

    data class DeliveryAddress(val data: DeliveryAddressData): PaymentUiState
}