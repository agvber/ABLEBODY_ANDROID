package com.smilehunter.ablebody.presentation.receipt.data

import com.smilehunter.ablebody.model.ReceiptData

sealed interface ReceiptUiState {

    object Loading: ReceiptUiState

    object LoadFail: ReceiptUiState

    data class Receipt(internal val data: ReceiptData): ReceiptUiState
}