package com.smilehunter.ablebody.presentation.receipt

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.GetReceiptUseCase
import com.smilehunter.ablebody.presentation.receipt.data.ReceiptUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getReceiptUseCase: GetReceiptUseCase
) : ViewModel() {

    private val _networkRefreshFlow = MutableSharedFlow<Unit>()
    private val networkRefreshFlow = _networkRefreshFlow.asSharedFlow()

    fun refreshNetwork() {
        viewModelScope.launch { _networkRefreshFlow.emit(Unit) }
    }

    private val receiptID = savedStateHandle.getStateFlow("content_id", "0")

    private val refreshReceiptNumber = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val receiptData: StateFlow<ReceiptUiState> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                combine(receiptID, refreshReceiptNumber) { id, _ -> getReceiptUseCase(id) }
                    .asResult()
                    .map {
                        when (it) {
                            is Result.Error -> ReceiptUiState.LoadFail(it.exception)
                            is Result.Loading -> ReceiptUiState.Loading
                            is Result.Success -> ReceiptUiState.Receipt(it.data)
                        }
                    }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                ReceiptUiState.Loading
            )

    fun refreshReceiptData() {
        viewModelScope.launch { refreshReceiptNumber.emit(refreshReceiptNumber.value + 1) }
    }
}