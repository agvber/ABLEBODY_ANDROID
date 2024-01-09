package com.smilehunter.ablebody.presentation.item_detail.data

import com.smilehunter.ablebody.model.ItemDetailData

sealed interface ItemDetailUiState {

    object Loading: ItemDetailUiState

    data class Error(val t: Throwable?): ItemDetailUiState

    data class Success(val data: ItemDetailData): ItemDetailUiState
}