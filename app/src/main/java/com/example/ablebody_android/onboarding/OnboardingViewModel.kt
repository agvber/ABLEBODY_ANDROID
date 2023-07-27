package com.example.ablebody_android.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ablebody_android.NetworkRepository
import com.example.ablebody_android.TokenSharedPreferencesRepository
import com.example.ablebody_android.onboarding.data.NicknameRule
import com.example.ablebody_android.onboarding.utils.checkNicknameRule
import com.example.ablebody_android.retrofit.dto.response.SendSMSResponse
import com.example.ablebody_android.retrofit.dto.response.UserDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.timerTask

class OnboardingViewModel(application: Application): AndroidViewModel(application) {

    private val tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(application.applicationContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val ioDispatcher = Dispatchers.IO

    private val timer = Timer()


    val availableNicknameCheckLiveData: LiveData<NicknameRule> get() = _availableNicknameCheckLiveData
    private val _availableNicknameCheckLiveData = MutableLiveData<NicknameRule>()

    fun checkAvailableNickname(name: String) {
        viewModelScope.launch(ioDispatcher) {
            var nicknameRule: NicknameRule = checkNicknameRule(name)

            if (nicknameRule == NicknameRule.Available) {
                nicknameRule = if (networkRepository.checkNickname(name).body()?.success == true) {
                    NicknameRule.Available
                } else {
                    NicknameRule.InUsed
                }
            }
            _availableNicknameCheckLiveData.postValue(nicknameRule)
        }
    }

    val sendSMSLiveData: LiveData<SendSMSResponse> get() =  _sendSMSLiveData
    private val _sendSMSLiveData = MutableLiveData<SendSMSResponse>()

    fun sendSMS(phoneNumber: String) {
        viewModelScope.launch(ioDispatcher) {
            val response = networkRepository.sendSMS(phoneNumber)
            _sendSMSLiveData.postValue(response.body())
        }
    }

    val currentCertificationNumberTimeLiveData: LiveData<Long> get() = _currentCertificationNumberTimeLiveData
    private val _currentCertificationNumberTimeLiveData = MutableLiveData<Long>(180000L)

    fun startCertificationNumberTimer() {
        viewModelScope.launch {
            val timerTask = timerTask {
                val currentTime = _currentCertificationNumberTimeLiveData.value?.minus(1000L)
                _currentCertificationNumberTimeLiveData.postValue(currentTime)
                if (currentTime == 0L) timer.cancel()
            }

            timer.schedule(timerTask, 1000L, 1000L)
        }
    }

    fun cancelTimer() { timer.cancel() }

    val userData: LiveData<UserDataResponse> get() = _userData
    private val _userData: MutableLiveData<UserDataResponse> = MutableLiveData()

    fun updateUserData(authToken: String) {
        viewModelScope.launch(ioDispatcher) {
            _userData.postValue(networkRepository.getUserData(authToken = authToken).body())
        }
    }


    fun putAuthToken(token: String) {
        viewModelScope.launch(ioDispatcher) {
            tokenSharedPreferencesRepository.putAuthToken(token)
        }
    }

    fun getAuthToken() = tokenSharedPreferencesRepository.getAuthToken()
}