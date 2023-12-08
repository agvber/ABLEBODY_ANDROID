package com.smilehunter.ablebody.presentation.my

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.domain.AddCouponUseCase
import com.smilehunter.ablebody.domain.GetCouponListUseCase
import com.smilehunter.ablebody.domain.GetOrderItemListUseCase
import com.smilehunter.ablebody.domain.GetUserBoardPagerUseCase
import com.smilehunter.ablebody.domain.GetUserInfoUseCase
import com.smilehunter.ablebody.model.CouponData
import com.smilehunter.ablebody.model.OrderItemData
import com.smilehunter.ablebody.model.UserBoardData
import com.smilehunter.ablebody.model.UserInfoData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class MyViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getCouponListUseCase: GetCouponListUseCase,
    private val getOrderItemListUseCase: GetOrderItemListUseCase,
    private val getUserBoardPagerUseCase: GetUserBoardPagerUseCase,
    private val addCouponUseCase: AddCouponUseCase,
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val uid = savedStateHandle.getStateFlow<String?>("uid", null)
    private val _userInfoLiveData = MutableLiveData<UserInfoData>()
    val userLiveData: LiveData<UserInfoData> = _userInfoLiveData

//    private val _couponListLiveData = MutableLiveData<List<CouponData>>()
//    val couponListLiveData: LiveData<List<CouponData>> = _couponListLiveData

    private val _orderItemListLiveData = MutableLiveData<List<OrderItemData>>()
    val orderItemListLiveData: LiveData<List<OrderItemData>> = _orderItemListLiveData

//    //마케팅 알림 동의 여부 받아오기
//    private val _getUserAdConsentLiveData = MutableLiveData<Boolean>()
//    val getUserAdConsentLiveData: LiveData<Boolean> = _getUserAdConsentLiveData

//    private val _suggestAppLiveData = MutableLiveData<String>()
//    val suggestAppLiveData: LiveData<String> = _suggestAppLiveData

//    private val _otherUserLiveData = MutableLiveData<UserInfoData>()
//    val otherUserLiveData: LiveData<UserInfoData> = _otherUserLiveData

    val userBoard: StateFlow<PagingData<UserBoardData.Content>> = getUserBoardPagerUseCase()
        .cachedIn(viewModelScope)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            PagingData.empty()
        )


    @OptIn(ExperimentalCoroutinesApi::class)
    val otherUserBoard: StateFlow<PagingData<UserBoardData.Content>> = uid.flatMapLatest {
        if (it != null) {
            getUserBoardPagerUseCase(it)
        }else{
            flowOf((PagingData.empty()))
        }
    }.cachedIn(viewModelScope)
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            PagingData.empty()
        )


    fun getData() {
        viewModelScope.launch {
            try {
                val userInfo = getUserInfoUseCase.invoke()
                _userInfoLiveData.postValue(userInfo)

                Log.d("userInfo", userInfo.toString())

//                val couponList = getCouponListUseCase.invoke()
//                _couponListLiveData.postValue(couponList)

                val orderItemList = getOrderItemListUseCase.invoke()
                _orderItemListLiveData.postValue(orderItemList)

//                val getUserAdConsent = userRepository.getUserAdConsent()
//                _getUserAdConsentLiveData.postValue(getUserAdConsent)
//                Log.d("getUserAdConsent", getUserAdConsent.toString())


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    fun getData(uid: StateFlow<String?>){
//        try {
//            viewModelScope.launch(ioDispatcher) {
//                Log.d("다른 유저 프로필getData",uid.toString())
//                val getOtherUserProfile = getUserInfoUseCase.invoke(uid.toString())
//                _otherUserLiveData.postValue(getOtherUserProfile)
//                getUserInfoUseCase(uid.toString())
//            }
//        }catch (e: java.lang.Exception){
//            e.printStackTrace()
//        }
//    }

//    fun changeUserAdConsent(value: Boolean) {
//        viewModelScope.launch(ioDispatcher) {
//            userRepository.acceptUserAdConsent(value)
//            _getUserAdConsentLiveData.postValue(value)
//            Log.d("UserAdConsent", value.toString())
//        }
//    }

//    fun sendSuggest(value: String) {
//        viewModelScope.launch(ioDispatcher) {
//            userRepository.suggestApp(value)
//            Log.d("sendSuggest", value)
//        }
//    }

//    suspend fun couponRegister(value: String): String = suspendCoroutine { continuation ->
//        var couponStatus = ""
//
//        viewModelScope.launch(ioDispatcher) {
//            couponStatus = when (val result = addCouponUseCase.invoke(value).toString()) {
//                "INVALID_COUPON_CODE" -> {
//                    Log.d("쿠폰 등록 invoke", result)
//                    "INVALID_COUPON_CODE"
//                }
//
//                "SUCCESS" -> {
//                    Log.d("쿠폰 등록 invoke", result)
//                    "SUCCESS"
//                }
//
//                else -> {
//                    // Handle any other cases if needed
//                    Log.d("쿠폰 등록 invoke", result)
//                    // 기본값 또는 다른 처리를 정의하세요.
//                    "OTHER_CASE"
//                }
//            }
//
//            continuation.resumeWith(Result.success(couponStatus))
//        }
//    }

//    fun resignUser(reason: String) {
//        viewModelScope.launch(ioDispatcher) {
//            Log.d("탈퇴 이유", reason)
//            userRepository.resignUser(reason)
//        }
//    }

//    fun otherUserProfile(uid: String){
//        viewModelScope.launch(ioDispatcher) {
////            val getOtherUserProfile = getUserInfoUseCase.invoke("")
////            _otherUserLiveData.postValue(getOtherUserProfile)
//            getUserInfoUseCase(uid)
//        }
//    }


}