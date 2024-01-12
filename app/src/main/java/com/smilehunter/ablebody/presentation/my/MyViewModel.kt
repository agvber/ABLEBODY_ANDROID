package com.smilehunter.ablebody.presentation.my

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.smilehunter.ablebody.data.dto.request.ReportRequest
import com.smilehunter.ablebody.data.repository.ManageRepository
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class MyViewModel @Inject constructor(
    private val manageRepository: ManageRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getOrderItemListUseCase: GetOrderItemListUseCase,
    private val getUserBoardPagerUseCase: GetUserBoardPagerUseCase,
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val uid = savedStateHandle.getStateFlow<String?>("uid", null)
    private val _userInfoLiveData = MutableLiveData<UserInfoData>()
    val userLiveData: LiveData<UserInfoData> = _userInfoLiveData

    private val _orderItemListLiveData = MutableLiveData<List<OrderItemData>>()
    val orderItemListLiveData: LiveData<List<OrderItemData>> = _orderItemListLiveData

    private val _sendErrorLiveData = MutableLiveData<Throwable?>()
    val sendErrorLiveData: LiveData<Throwable?> = _sendErrorLiveData
//
//    fun reportUser(reportRequest: ReportRequest){
//        Log.d("뷰모델에 신고 들어옴", reportRequest.reason)
//        viewModelScope.launch(ioDispatcher) {
//            manageRepository.report(reportRequest)
//        }
//    }


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

                val orderItemList = getOrderItemListUseCase.invoke()
                _orderItemListLiveData.postValue(orderItemList)
                _sendErrorLiveData.postValue(null)

            } catch (e: Exception) {
                e.printStackTrace()
                _sendErrorLiveData.postValue(e)
            }
        }
    }


}