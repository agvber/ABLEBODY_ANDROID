package com.smilehunter.ablebody.presentation.payment.data

import com.smilehunter.ablebody.model.CouponData

sealed interface CouponBagsUiState {

    object Loading: CouponBagsUiState

    data class LoadFail(val t: Throwable?): CouponBagsUiState

    data class Coupons(val data: List<CouponData>): CouponBagsUiState
}