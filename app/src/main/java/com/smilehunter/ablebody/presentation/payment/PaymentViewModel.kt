package com.smilehunter.ablebody.presentation.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.smilehunter.ablebody.data.dto.request.AddOrderListRequest
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.ConfirmPaymentUseCase
import com.smilehunter.ablebody.domain.GetCouponListUseCase
import com.smilehunter.ablebody.domain.GetMyDeliveryAddressUseCase
import com.smilehunter.ablebody.domain.GetUserInfoUseCase
import com.smilehunter.ablebody.domain.HandlePaymentFailureUseCase
import com.smilehunter.ablebody.domain.OrderItemUseCase
import com.smilehunter.ablebody.model.CouponData
import com.smilehunter.ablebody.presentation.payment.data.PaymentPassthroughData
import com.smilehunter.ablebody.presentation.payment.data.PaymentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.roundToInt

@HiltViewModel
class PaymentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCouponListUseCase: GetCouponListUseCase,
    getUserInfoUseCase: GetUserInfoUseCase,
    getMyDeliveryAddressUseCase: GetMyDeliveryAddressUseCase,
    private val orderItemUseCase: OrderItemUseCase,
    private val confirmPaymentUseCase: ConfirmPaymentUseCase,
    private val handlePaymentFailureUseCase: HandlePaymentFailureUseCase
): ViewModel() {

    private val _networkRefreshFlow = MutableSharedFlow<Unit>()
    private val networkRefreshFlow = _networkRefreshFlow.asSharedFlow()

    fun refreshNetwork() {
        viewModelScope.launch { _networkRefreshFlow.emit(Unit) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val paymentPassthroughData: StateFlow<PaymentPassthroughData?> =
        savedStateHandle.getStateFlow<String?>("payment_passthrough_data", null)
            .flatMapLatest {
                flow {
                    if (it != null) {
                        emit(Gson().fromJson(it, PaymentPassthroughData::class.java))
                    }
                }
            }
            .asResult()
            .map {
                when (it) {
                    is Result.Success -> it.data
                    else -> null
                }
            }
            .stateIn(
                viewModelScope,
                started = SharingStarted.Eagerly,
                null
            )

    private val _couponID = MutableStateFlow<Int?>(null)
    val couponID = _couponID.asStateFlow()

    fun updateCouponID(value: Int?) {
        viewModelScope.launch { _couponID.emit(value) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val couponDiscountPrice: StateFlow<Int> = couponID.flatMapLatest { couponID ->
        if (couponID == null) { return@flatMapLatest flowOf(0) }

        val selectedCoupon = (coupons.value as PaymentUiState.Coupons).data.first { it.id == couponID }

        return@flatMapLatest when (selectedCoupon.discountType) {
            CouponData.DiscountType.PRICE -> flowOf(selectedCoupon.discountAmount.unaryMinus())
            CouponData.DiscountType.RATE -> {
                val totalPrice = paymentPassthroughData.value?.totalPrice ?: 0
                val discountRate = (totalPrice * (selectedCoupon.discountAmount.toDouble() / 100))
                    .roundToInt()
                    .unaryMinus()
                flowOf(discountRate)
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    private val _userPointTextValue = MutableStateFlow("")
    val userPointTextValue = _userPointTextValue.asStateFlow()

    fun updateUserPointTextValue(value: String) {
        viewModelScope.launch {
            val hasPoint = (userData.value as? PaymentUiState.User)?.data?.creatorPoint ?: 0
            if ((value.toLongOrNull() ?: 0) > hasPoint) {
                _userPointTextValue.emit(hasPoint.toString())
            } else {
                _userPointTextValue.emit(value)
            }
        }
    }

    fun calculatorUserPoint() {
        viewModelScope.launch {
            val item = paymentPassthroughData.value?.items?.firstOrNull()
            val itemPrice = item?.salePrice ?: item?.price ?: 0
            val itemMaximumDiscount = itemPrice * (5.0 / 100)
            if ((userPointTextValue.value.toIntOrNull() ?: 0) > itemMaximumDiscount) {
                _userPointTextValue.emit(itemMaximumDiscount.roundToInt().toString())
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val userData: StateFlow<PaymentUiState> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                flow { emit(getUserInfoUseCase()) }
                    .asResult()
                    .map {
                        when(it) {
                            is Result.Error -> PaymentUiState.LoadFail
                            is Result.Loading -> PaymentUiState.Loading
                            is Result.Success -> PaymentUiState.User(it.data)
                        }
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PaymentUiState.Loading
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val coupons: StateFlow<PaymentUiState> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                paymentPassthroughData.flatMapLatest { PassthroughData ->
                    flowOf(
                        getCouponListUseCase().filter {
                            if (it.brand == null) {
                                return@filter true
                            }

                            it.brand == PassthroughData?.items?.firstOrNull()?.brandName
                        }
                    )
                }
                    .asResult()
                    .map {
                        when(it) {
                            is Result.Error -> PaymentUiState.LoadFail
                            is Result.Loading -> PaymentUiState.Loading
                            is Result.Success -> PaymentUiState.Coupons(it.data)
                        }
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PaymentUiState.Loading
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val deliveryAddress: StateFlow<PaymentUiState> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                flow { emit(getMyDeliveryAddressUseCase()) }
                    .asResult()
                    .map {
                        when(it) {
                            is Result.Error -> PaymentUiState.LoadFail
                            is Result.Loading -> PaymentUiState.Loading
                            is Result.Success -> PaymentUiState.DeliveryAddress(it.data)
                        }
                    }
            }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PaymentUiState.Loading
        )

    private val _orderItemID = MutableStateFlow("")
    val orderItemID = _orderItemID.asStateFlow()

    fun orderItem(
        orderName: String,
        paymentType: String,
        paymentMethod: String,
        easyPayType: String?,
        amountOfPayment: Int,
    ) {
        viewModelScope.launch {
            try {
                val address = (deliveryAddress.value as PaymentUiState.DeliveryAddress).data

                val addOrderListRequest = AddOrderListRequest(
                    addressId = address.id,
                    orderName = orderName,
                    paymentType = paymentType,
                    paymentMethod = paymentMethod,
                    easyPayType = easyPayType,
                    pointDiscount = userPointTextValue.value.toIntOrNull() ?: 0,
                    deliveryPrice = paymentPassthroughData.value!!.deliveryPrice,
                    orderListItemReqDtoList = paymentPassthroughData.value!!.items.map {
                        AddOrderListRequest.OrderListItemReqDto(
                            itemId = it.itemID,
                            couponBagsId = couponID.value,
                            colorOption = it.getContentOption(PaymentPassthroughData.ItemOptions.Option.COLOR),
                            sizeOption = it.getContentOption(PaymentPassthroughData.ItemOptions.Option.SIZE),
                            itemPrice = it.price,
                            itemCount = it.count,
                            itemDiscount = abs(it.differencePrice),
                            couponDiscount = couponDiscountPrice.value,
                            amount = amountOfPayment
                        )
                    }
                )

                val response = orderItemUseCase(addOrderListRequest)
                _orderItemID.emit(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun PaymentPassthroughData.Item
            .getContentOption(option: PaymentPassthroughData.ItemOptions.Option): String? {

        return this.options.firstOrNull { it.options ==  option }?.content
    }


    fun confirmPayment(
        paymentKey: String,
        orderListId: String,
        amount: String
    ) {
        viewModelScope.launch {
            try {
                confirmPaymentUseCase(
                    paymentKey = paymentKey,
                    orderListId = orderListId,
                    amount = amount
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun handlePaymentFailure(
        code: String,
        message: String,
        orderListId: String
    ) {
        viewModelScope.launch {
            try {
                handlePaymentFailureUseCase(
                    code = code,
                    message = message,
                    orderListId = orderListId
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}