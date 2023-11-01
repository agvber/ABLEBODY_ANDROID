package com.smilehunter.ablebody.presentation.order_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.OrderManagementRepository
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.GetDeliveryTrackingNumberUseCase
import com.smilehunter.ablebody.domain.GetOrderItemListUseCase
import com.smilehunter.ablebody.presentation.order_management.data.OrderManagementUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderManagementViewModel @Inject constructor(
    getOrderItemListUseCase: GetOrderItemListUseCase,
    private val getDeliveryTrackingNumberUseCase: GetDeliveryTrackingNumberUseCase,
    private val orderManagementRepository: OrderManagementRepository
): ViewModel() {

    private val refreshOrderItemNumber = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val orderItems: StateFlow<OrderManagementUiState> =
        refreshOrderItemNumber.flatMapLatest { _ ->
            flowOf(getOrderItemListUseCase())
        }
            .asResult()
            .map {
                when (it) {
                    is Result.Error -> OrderManagementUiState.LoadFail
                    is Result.Loading -> OrderManagementUiState.Loading
                    is Result.Success -> OrderManagementUiState.OrderItems(it.data)
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                OrderManagementUiState.Loading
            )

    fun cancelOrderItem(id: String) {
        viewModelScope.launch {
            orderManagementRepository.cancelOrderItem(id)
        }
    }

    fun refreshOrderItems() {
        viewModelScope.launch { refreshOrderItemNumber.emit(refreshOrderItemNumber.value + 1) }
    }

    private val _deliveryTrackingData = MutableStateFlow<OrderManagementUiState>(OrderManagementUiState.Loading)
    val deliveryTrackingData = _deliveryTrackingData.asStateFlow()

    fun updateDeliveryTrackingID(id: String) {
        viewModelScope.launch {
            try {
                _deliveryTrackingData.emit(
                    OrderManagementUiState.DeliveryTracking(getDeliveryTrackingNumberUseCase(id))
                )

            } catch (e: Exception) {
                _deliveryTrackingData.emit(OrderManagementUiState.LoadFail)
                e.printStackTrace()
            }
        }
    }

}