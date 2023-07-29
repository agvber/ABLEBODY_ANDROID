package com.example.ablebody_android.onboarding

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ablebody_android.NetworkRepository
import com.example.ablebody_android.TokenSharedPreferencesRepository
import com.example.ablebody_android.onboarding.data.NicknameRule
import com.example.ablebody_android.onboarding.utils.checkNicknameRule
import com.example.ablebody_android.retrofit.dto.response.CheckSMSResponse
import com.example.ablebody_android.retrofit.dto.response.SendSMSResponse
import com.example.ablebody_android.retrofit.dto.response.UserDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class OnboardingViewModel(application: Application): AndroidViewModel(application) {

    private val tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(application.applicationContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val ioDispatcher = Dispatchers.IO

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

    val sendSMSLiveData: LiveData<Response<SendSMSResponse>> get() =  _sendSMSLiveData
    private val _sendSMSLiveData = MutableLiveData<Response<SendSMSResponse>>()

    fun sendSMS(phoneNumber: String) {
        viewModelScope.launch(ioDispatcher) {
            val response = networkRepository.sendSMS(phoneNumber)
            _sendSMSLiveData.postValue(response)
        }
    }

    val currentCertificationNumberTimeLiveData: LiveData<Long> get() = _currentCertificationNumberTimeLiveData
    private val _currentCertificationNumberTimeLiveData = MutableLiveData<Long>(180000L)

    private var certificationNumberCountDownTimerIsRunning: Boolean = false

    private val certificationNumberCountDownTimer = object : CountDownTimer(180000L, 1000L) {
        override fun onTick(millisUntilFinished: Long) {
            val currentTime = _currentCertificationNumberTimeLiveData.value?.minus(1000L)
            _currentCertificationNumberTimeLiveData.postValue(currentTime)
        }
        override fun onFinish() {
            certificationNumberCountDownTimerIsRunning = false
        }
    }

    fun startCertificationNumberTimer() {
        if (!certificationNumberCountDownTimerIsRunning) {
            _currentCertificationNumberTimeLiveData.value = 180000L
            certificationNumberCountDownTimerIsRunning = true
            certificationNumberCountDownTimer.start()
        }
    }

    fun cancelCertificationNumberCountDownTimer() {
        if (certificationNumberCountDownTimerIsRunning) {
            certificationNumberCountDownTimer.cancel()
            certificationNumberCountDownTimerIsRunning = false
        }
    }

    val checkSMSLiveData: LiveData<Response<CheckSMSResponse>> get() = _checkSMSLiveData
    private val _checkSMSLiveData = MutableLiveData<Response<CheckSMSResponse>>()

    fun checkSMS(phoneConfirmId: Long, verifyingCode: String){
        viewModelScope.launch(ioDispatcher) {
            val response = networkRepository.checkSMS(phoneConfirmId = phoneConfirmId, verifyingCode = verifyingCode)
            _checkSMSLiveData.postValue(response)
        }
    }

    fun clearCheckSMSLiveData() {
        _checkSMSLiveData.value = null
    }

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

    override fun onCleared() {
        super.onCleared()
        cancelCertificationNumberCountDownTimer()
    }
}