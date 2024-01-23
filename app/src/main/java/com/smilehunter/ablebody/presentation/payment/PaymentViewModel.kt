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
import com.smilehunter.ablebody.model.UserInfoData
import com.smilehunter.ablebody.presentation.payment.data.CouponBagsUiState
import com.smilehunter.ablebody.presentation.payment.data.DeliveryAddressUiState
import com.smilehunter.ablebody.presentation.payment.data.PaymentPassthroughData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
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
    val userData: StateFlow<UserInfoData?> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                flow<UserInfoData?> { emit(getUserInfoUseCase()) }
                    .catch { emit(null) }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val paymentPassthroughData: StateFlow<PaymentPassthroughData?> =
        savedStateHandle.getStateFlow("payment_passthrough_data", null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val coupons: StateFlow<CouponBagsUiState> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                paymentPassthroughData.flatMapLatest { data ->
                    flowOf(
                        getCouponListUseCase().filter {
                            if (it.brand == null) {
                                return@filter true
                            }

                            it.brand == data?.items?.firstOrNull()?.brandName
                        }
                    )
                }
                    .asResult()
                    .map {
                        when(it) {
                            is Result.Error -> CouponBagsUiState.LoadFail(it.exception)
                            is Result.Loading -> CouponBagsUiState.Loading
                            is Result.Success -> CouponBagsUiState.Coupons(it.data)
                        }
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CouponBagsUiState.Loading
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val deliveryAddress: StateFlow<DeliveryAddressUiState> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                flow { emit(getMyDeliveryAddressUseCase()) }
                    .asResult()
                    .map {
                        when(it) {
                            is Result.Error -> DeliveryAddressUiState.LoadFail(it.exception)
                            is Result.Loading -> DeliveryAddressUiState.Loading
                            is Result.Success -> DeliveryAddressUiState.Success(it.data)
                        }
                    }
            }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DeliveryAddressUiState.Loading
        )

    private val _couponID = MutableStateFlow<Int?>(null)
    val couponID = _couponID.asStateFlow()

    fun updateCouponID(value: Int?) {
        viewModelScope.launch { _couponID.emit(value) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val couponDiscountPrice: StateFlow<Int> =
        coupons.flatMapLatest { couponBags ->
            couponID.map { couponID ->
                if (couponID == null || couponBags !is CouponBagsUiState.Coupons) {
                    return@map 0
                }

                val selectedCoupon = couponBags.data.first { it.id == couponID }
                when (selectedCoupon.discountType) {
                    CouponData.DiscountType.PRICE -> selectedCoupon.discountAmount
                    CouponData.DiscountType.RATE -> {
                        val totalPrice = paymentPassthroughData.value?.totalPrice ?: 0
                        val discountRate = (totalPrice * (selectedCoupon.discountAmount.toDouble() / 100)).roundToInt()
                        discountRate
                    }
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
            val hasPoint = userData.value?.creatorPoint ?: 0
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


    private val _orderItemID = MutableStateFlow("")
    val orderItemID = _orderItemID.asStateFlow()

    fun orderItem(
        orderName: String,
        paymentType: String,
        paymentMethod: String,
        easyPayType: String?,
        amount: Int,
    ) {
        viewModelScope.launch {
            try {
                val address = (deliveryAddress.value as DeliveryAddressUiState.Success).data

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
                            itemDiscount = it.differencePrice,
                            couponDiscount = couponDiscountPrice.value,
                            amount = amount
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

    private val _paymentSuccess = MutableSharedFlow<Unit>()
    val paymentSuccess = _paymentSuccess.asSharedFlow()

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
                _paymentSuccess.emit(Unit)
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