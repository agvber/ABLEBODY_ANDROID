package com.smilehunter.ablebody.presentation.my.alarm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.domain.AddCouponUseCase
import com.smilehunter.ablebody.domain.GetCouponListUseCase
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    //마케팅 알림 동의 여부 받아오기
    private val _getUserAdConsentLiveData = MutableLiveData<Boolean>()
    val getUserAdConsentLiveData: LiveData<Boolean> = _getUserAdConsentLiveData

    private val _sendErrorLiveData = MutableLiveData<Throwable?>()
    val sendErrorLiveData: LiveData<Throwable?> = _sendErrorLiveData

    fun getAlarmData() {
        viewModelScope.launch {
            try {
                val getUserAdConsent = userRepository.getUserAdConsent()
                _getUserAdConsentLiveData.postValue(getUserAdConsent)
                Log.d("getUserAdConsent", getUserAdConsent.toString())
                _sendErrorLiveData.postValue(null)

            } catch (e: Exception) {
                e.printStackTrace()
                _sendErrorLiveData.postValue(e)
            }
        }
    }

    fun changeUserAdConsent(value: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            userRepository.acceptUserAdConsent(value)
            _getUserAdConsentLiveData.postValue(value)
            Log.d("UserAdConsent", value.toString())
        }
    }
}