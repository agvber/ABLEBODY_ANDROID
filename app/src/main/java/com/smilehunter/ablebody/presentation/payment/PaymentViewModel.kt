package com.smilehunter.ablebody.presentation.payment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.GetCouponListUseCase
import com.smilehunter.ablebody.domain.GetMyDeliveryAddressUseCase
import com.smilehunter.ablebody.domain.GetUserInfoUseCase
import com.smilehunter.ablebody.domain.OrderItemUseCase
import com.smilehunter.ablebody.model.CouponData
import com.smilehunter.ablebody.presentation.payment.data.PaymentPassthroughData
import com.smilehunter.ablebody.presentation.payment.data.PaymentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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
    private val orderItemUseCase: OrderItemUseCase
): ViewModel() {

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

    private val _couponID = MutableStateFlow(-1)
    val couponID = _couponID.asStateFlow()

    fun updateCouponID(value: Int) {
        viewModelScope.launch { _couponID.emit(value) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val couponDiscountPrice: StateFlow<Int> = couponID.flatMapLatest { couponID ->
        if (couponID == -1) { return@flatMapLatest flowOf(0) }

        val selectedCoupon = (coupons.value as PaymentUiState.Coupons).data.first { it.id == couponID }

        return@flatMapLatest when (selectedCoupon.discountType) {
            CouponData.DiscountType.PRICE -> flowOf(selectedCoupon.discountAmount.unaryMinus())
            CouponData.DiscountType.RATE -> flowOf((paymentPassthroughData.value!!.price * (selectedCoupon.discountAmount.toDouble() / 100)).roundToInt().unaryMinus())
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

    fun calculatorUserPoint(itemPrice: Int) {
        viewModelScope.launch {
            val itemMaximumDiscount = itemPrice * (5.0 / 100)
            if ((userPointTextValue.value.toIntOrNull() ?: 0) > itemMaximumDiscount) {
                _userPointTextValue.emit(itemMaximumDiscount.roundToInt().toString())
            }
        }
    }

    private val _bankTextValue = MutableStateFlow("")
    val bankTextValue = _bankTextValue.asStateFlow()

    fun updateBankTextValue(value: String) {
        viewModelScope.launch { _bankTextValue.emit(value) }
    }

    private val _accountNumberTextValue = MutableStateFlow("")
    val accountNumberTextValue = _accountNumberTextValue.asStateFlow()

    fun updateAccountNumberTextValue(value: String) {
        viewModelScope.launch { _accountNumberTextValue.emit(value) }
    }

    private val _accountHolderTextValue = MutableStateFlow("")
    val accountHolderTextValue = _accountHolderTextValue.asStateFlow()

    fun updateAccountHolderTextValue(value: String) {
        viewModelScope.launch { _accountHolderTextValue.emit(value) }
    }


    val userData: StateFlow<PaymentUiState> =
        flow { emit(getUserInfoUseCase()) }
            .asResult()
            .map {
                when(it) {
                    is Result.Error -> PaymentUiState.LoadFail
                    is Result.Loading -> PaymentUiState.Loading
                    is Result.Success -> PaymentUiState.User(it.data)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PaymentUiState.Loading
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val coupons: StateFlow<PaymentUiState> =
        paymentPassthroughData.flatMapLatest { data ->
            flowOf(
                getCouponListUseCase().filter {
                    if (it.brand == null) true else it.brand == data!!.brandName
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
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PaymentUiState.Loading
            )

    val deliveryAddress: StateFlow<PaymentUiState> = flow { emit(getMyDeliveryAddressUseCase()) }
        .asResult()
        .map {
            when(it) {
                is Result.Error -> PaymentUiState.LoadFail
                is Result.Loading -> PaymentUiState.Loading
                is Result.Success -> PaymentUiState.DeliveryAddress(it.data)
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
        paymentMethod: String,
        amountOfPayment: Int,
    ) {
        viewModelScope.launch {
            try {
                val address = (deliveryAddress.value as PaymentUiState.DeliveryAddress).data

                orderItemUseCase(
                    itemID = paymentPassthroughData.value!!.itemID,
                    addressID = address.id,
                    couponBagsID = couponID.value.let { if (it == -1) null else it },
                    refundBankName = bankTextValue.value,
                    refundAccount = accountNumberTextValue.value,
                    refundAccountHolder = accountHolderTextValue.value,
                    paymentMethod = paymentMethod,
                    price = paymentPassthroughData.value!!.price,
                    itemDiscount = paymentPassthroughData.value!!.differencePrice,
                    couponDiscount = couponDiscountPrice.value,
                    pointDiscount = userPointTextValue.value.toIntOrNull() ?: 0,
                    deliveryPrice = 3000,
                    amountOfPayment = amountOfPayment,
                    itemOptionIdList = paymentPassthroughData.value!!.itemIDOptions
                ).let { _orderItemID.emit(it) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}