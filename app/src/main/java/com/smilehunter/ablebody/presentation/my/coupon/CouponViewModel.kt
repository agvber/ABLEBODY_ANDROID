package com.smilehunter.ablebody.presentation.my.coupon

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.domain.AddCouponUseCase
import com.smilehunter.ablebody.domain.CouponStatus
import com.smilehunter.ablebody.domain.GetCouponListUseCase
import com.smilehunter.ablebody.domain.GetOrderItemListUseCase
import com.smilehunter.ablebody.domain.GetUserBoardPagerUseCase
import com.smilehunter.ablebody.domain.GetUserInfoUseCase
import com.smilehunter.ablebody.model.CouponData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class CouponViewModel @Inject constructor(
    private val getCouponListUseCase: GetCouponListUseCase,
    private val addCouponUseCase: AddCouponUseCase,
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val _couponListLiveData = MutableLiveData<List<CouponData>>()
    val couponListLiveData: LiveData<List<CouponData>> = _couponListLiveData

    private val _sendErrorLiveData = MutableLiveData<Throwable?>()
    val sendErrorLiveData: LiveData<Throwable?> = _sendErrorLiveData

    private val _couponStateLiveData = MutableLiveData<CouponStatus>()
    val couponStateLiveData: LiveData<CouponStatus> = _couponStateLiveData

    fun getCouponData() {
        viewModelScope.launch {
            try {
                val couponList = getCouponListUseCase.invoke()
                _couponListLiveData.postValue(couponList)
                _sendErrorLiveData.postValue(null)

            } catch (e: Exception) {
                e.printStackTrace()
                _sendErrorLiveData.postValue(e)
            }
        }
    }

    fun couponRegister(value: String) {

        viewModelScope.launch(ioDispatcher) {
            try{
                val result = addCouponUseCase.invoke(value)
                _couponStateLiveData.postValue(result)
                _sendErrorLiveData.postValue(null)
            } catch (e: Exception) {
                e.printStackTrace()
                _sendErrorLiveData.postValue(e)
            }
        }
    }
}