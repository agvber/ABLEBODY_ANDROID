package com.smilehunter.ablebody.presentation.my.myInfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.smilehunter.ablebody.data.repository.OnboardingRepository
import com.smilehunter.ablebody.data.repository.TokenRepository
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val tokenRepository: TokenRepository,
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _userInfoLiveData = MutableLiveData<UserInfoData>()
    val userLiveData: LiveData<UserInfoData> = _userInfoLiveData

    fun getMyInfoData() {
        viewModelScope.launch {
            try {
                val userInfo = getUserInfoUseCase.invoke()
                _userInfoLiveData.postValue(userInfo)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resignUser(reason: String) {
        viewModelScope.launch(ioDispatcher) {
            Log.d("탈퇴 이유", reason)
            userRepository.resignUser(reason)
        }
    }

    fun deleteToken() {
        viewModelScope.launch {
            tokenRepository.deleteToken()
        }
    }

}