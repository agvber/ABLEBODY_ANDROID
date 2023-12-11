package com.smilehunter.ablebody.presentation.order_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.OrderManagementRepository
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.GetDeliveryTrackingNumberUseCase
import com.smilehunter.ablebody.domain.GetOrderItemListUseCase
import com.smilehunter.ablebody.presentation.order_management.data.DeliveryTrackingUiState
import com.smilehunter.ablebody.presentation.order_management.data.OrderItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderManagementViewModel @Inject constructor(
    getOrderItemListUseCase: GetOrderItemListUseCase,
    private val getDeliveryTrackingNumberUseCase: GetDeliveryTrackingNumberUseCase,
    private val orderManagementRepository: OrderManagementRepository
): ViewModel() {

    private val _networkRefreshFlow = MutableSharedFlow<Unit>()
    private val networkRefreshFlow = _networkRefreshFlow.asSharedFlow()

    fun refreshNetwork() {
        viewModelScope.launch { _networkRefreshFlow.emit(Unit) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val orderItems: StateFlow<OrderItemUiState> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest { _ ->
                flowOf(getOrderItemListUseCase())
            }
                .asResult()
                .map {
                    when (it) {
                        is Result.Error -> OrderItemUiState.LoadFail(it.exception)
                        is Result.Loading -> OrderItemUiState.Loading
                        is Result.Success -> OrderItemUiState.Success(it.data)
                    }
                }
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5_000),
                    OrderItemUiState.Loading
                )

    fun cancelOrderItem(id: String) {
        viewModelScope.launch {
            orderManagementRepository.cancelOrderItem(id)
        }
    }

    fun refreshOrderItems() {
        viewModelScope.launch { _networkRefreshFlow.emit(Unit) }
    }

    private val _deliveryTrackingData = MutableStateFlow<DeliveryTrackingUiState>(DeliveryTrackingUiState.Loading)
    val deliveryTrackingData = _deliveryTrackingData.asStateFlow()

    fun updateDeliveryTrackingID(id: String) {
        viewModelScope.launch {
            try {
                _deliveryTrackingData.emit(
                    DeliveryTrackingUiState.Success(getDeliveryTrackingNumberUseCase(id))
                )

            } catch (e: Exception) {
                _deliveryTrackingData.emit(DeliveryTrackingUiState.LoadFail(e))
                e.printStackTrace()
            }
        }
    }

}