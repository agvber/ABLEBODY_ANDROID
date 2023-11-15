package com.smilehunter.ablebody.presentation.my

import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.domain.GetCouponListUseCase
import com.smilehunter.ablebody.domain.GetOrderItemListUseCase
import com.smilehunter.ablebody.domain.GetUserBoardPagerUseCase
import com.smilehunter.ablebody.domain.GetUserInfoUseCase
import com.smilehunter.ablebody.domain.ItemData
import com.smilehunter.ablebody.model.CouponData
import com.smilehunter.ablebody.model.OrderItemData
import com.smilehunter.ablebody.model.UserBoardData
import com.smilehunter.ablebody.model.UserInfoData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import com.smilehunter.ablebody.presentation.item_detail.ui.ItemDetailViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getCouponListUseCase: GetCouponListUseCase,
    private val getOrderItemListUseCase: GetOrderItemListUseCase,
    private val getUserBoardPagerUseCase: GetUserBoardPagerUseCase,
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher

): ViewModel() {

    private val _userInfoLiveData = MutableLiveData<UserInfoData>()
    val userLiveData: LiveData<UserInfoData> = _userInfoLiveData

    private val _couponListLiveData = MutableLiveData<List<CouponData>>()
    val couponListLiveData: LiveData<List<CouponData>> = _couponListLiveData


    private val _orderItemListLiveData = MutableLiveData<List<OrderItemData>>()
    val orderItemListLiveData: LiveData<List<OrderItemData>> = _orderItemListLiveData

    val userBoard: StateFlow<PagingData<UserBoardData.Content>>
            = getUserBoardPagerUseCase()
        .cachedIn(viewModelScope)
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

                val couponList = getCouponListUseCase.invoke()
                _couponListLiveData.postValue(couponList)

                val orderItemList = getOrderItemListUseCase.invoke()
                _orderItemListLiveData.postValue(orderItemList)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}