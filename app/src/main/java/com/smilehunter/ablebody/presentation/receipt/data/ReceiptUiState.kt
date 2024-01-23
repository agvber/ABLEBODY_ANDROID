package com.smilehunter.ablebody.presentation.receipt.data

import com.smilehunter.ablebody.model.ReceiptData

sealed interface ReceiptUiState {

    object Loading: ReceiptUiState

    data class LoadFail(val t: Throwable?): ReceiptUiState

    data class Receipt(internal val data: ReceiptData): ReceiptUiState
}