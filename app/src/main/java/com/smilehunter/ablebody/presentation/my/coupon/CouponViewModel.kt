package com.smilehunter.ablebody.presentation.my.coupon

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.domain.AddCouponUseCase
import com.smilehunter.ablebody.domain.GetCouponListUseCase
import com.smilehunter.ablebody.domain.GetOrderItemListUseCase
import com.smilehunter.ablebody.domain.GetUserBoardPagerUseCase
import com.smilehunter.ablebody.domain.GetUserInfoUseCase
import com.smilehunter.ablebody.model.CouponData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class CouponViewModel @Inject constructor(
//    private val userRepository: UserRepository,
//    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getCouponListUseCase: GetCouponListUseCase,
//    private val getOrderItemListUseCase: GetOrderItemListUseCase,
//    private val getUserBoardPagerUseCase: GetUserBoardPagerUseCase,
    private val addCouponUseCase: AddCouponUseCase,
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _couponListLiveData = MutableLiveData<List<CouponData>>()
    val couponListLiveData: LiveData<List<CouponData>> = _couponListLiveData

    fun getCouponData() {
        viewModelScope.launch {
            try {
                val couponList = getCouponListUseCase.invoke()
                _couponListLiveData.postValue(couponList)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun couponRegister(value: String): String = suspendCoroutine { continuation ->
        var couponStatus = ""

        viewModelScope.launch(ioDispatcher) {
            couponStatus = when (val result = addCouponUseCase.invoke(value).toString()) {
                "INVALID_COUPON_CODE" -> {
                    Log.d("쿠폰 등록 invoke", result)
                    "INVALID_COUPON_CODE"
                }

                "SUCCESS" -> {
                    Log.d("쿠폰 등록 invoke", result)
                    "SUCCESS"
                }

                else -> {
                    // Handle any other cases if needed
                    Log.d("쿠폰 등록 invoke", result)
                    // 기본값 또는 다른 처리를 정의하세요.
                    "OTHER_CASE"
                }
            }

            continuation.resumeWith(Result.success(couponStatus))
        }
    }
}