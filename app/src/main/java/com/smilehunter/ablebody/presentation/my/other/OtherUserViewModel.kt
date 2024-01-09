package com.smilehunter.ablebody.presentation.my.other

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

@HiltViewModel
class OtherUserViewModel @Inject constructor(
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
    private val _otherUserLiveData = MutableLiveData<UserInfoData>()
    val otherUserLiveData: LiveData<UserInfoData> = _otherUserLiveData

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
    fun otherUserProfile(uid: String){
        viewModelScope.launch(ioDispatcher) {
            val getOtherUserProfile = getUserInfoUseCase.invoke(uid)
            _otherUserLiveData.postValue(getOtherUserProfile)
            getUserInfoUseCase(uid)
        }
    }
    fun getData() {
        viewModelScope.launch {
            try {
//                val userInfo = getUserInfoUseCase.invoke()
//                _userInfoLiveData.postValue(userInfo)
//
//                Log.d("userInfo", userInfo.toString())

//                val orderItemList = getOrderItemListUseCase.invoke()
//                _orderItemListLiveData.postValue(orderItemList)


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}