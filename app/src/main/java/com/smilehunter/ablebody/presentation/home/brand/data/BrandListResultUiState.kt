package com.smilehunter.ablebody.presentation.home.brand.data

import com.smilehunter.ablebody.model.BrandListData

sealed interface BrandListResultUiState {
    object Loading: BrandListResultUiState

    data class Error(val t: Throwable?): BrandListResultUiState

    data class Success(
        val data: List<BrandListData>
    ): BrandListResultUiState
}