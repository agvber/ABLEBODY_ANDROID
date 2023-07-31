package com.example.ablebody_android.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ablebody_android.Gender
import com.example.ablebody_android.NetworkRepository
import com.example.ablebody_android.TokenSharedPreferencesRepository
import com.example.ablebody_android.onboarding.data.CertificationNumberInfoMessageUiState
import com.example.ablebody_android.onboarding.data.NicknameRule
import com.example.ablebody_android.onboarding.data.ProfileImages
import com.example.ablebody_android.onboarding.utils.CertificationNumberCountDownTimer
import com.example.ablebody_android.onboarding.utils.convertMillisecondsToFormattedTime
import com.example.ablebody_android.onboarding.utils.isNicknameRuleMatch
import com.example.ablebody_android.retrofit.dto.response.CheckSMSResponse
import com.example.ablebody_android.retrofit.dto.response.SendSMSResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class OnboardingViewModel(application: Application): AndroidViewModel(application) {

    private val tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(application.applicationContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val ioDispatcher = Dispatchers.IO

    val phoneNumberState: StateFlow<String> get() =  _phoneNumberState.asStateFlow()
    private val _phoneNumberState = MutableStateFlow<String>("")

    fun updatePhoneNumber(phoneNumber: String) {
        viewModelScope.launch { _phoneNumberState.emit(phoneNumber) }
    }

    private val _currentCertificationNumberTimeLiveData = MutableStateFlow(180000L)
    private val currentCertificationNumberTimeLiveData = _currentCertificationNumberTimeLiveData.asStateFlow()

    private val certificationNumberCountDownTimer = CertificationNumberCountDownTimer()

    fun startCertificationNumberTimer() {
        certificationNumberCountDownTimer.startCertificationNumberTimer()
        certificationNumberCountDownTimer.registerOnChangeListener(object : CertificationNumberCountDownTimer.Callback {
            override fun onChangeValue(value: Long) {
                _currentCertificationNumberTimeLiveData.value = value
            }
        })
        certificationNumberCheckJob.start()
    }

    fun cancelCertificationNumberCountDownTimer() {
        certificationNumberCountDownTimer.unRegisterOnChangeListener()
        certificationNumberCountDownTimer.cancelCertificationNumberCountDownTimer()
        certificationNumberCheckJob.cancel()
    }

    val certificationNumberState: StateFlow<String> get() =  _certificationNumberState.asStateFlow()
    private val _certificationNumberState = MutableStateFlow<String>("")

    private val certificationNumberCheckJob = viewModelScope.launch {
        certificationNumberState.collect { number ->
            if (number.length == 4) {
                sendSMSLiveData.value?.body()?.data?.phoneConfirmId?.let { phoneConfirmId ->
                    checkSMS(phoneConfirmId, certificationNumberState.value)
                }
            }
        }
    }

    fun updateCertificationNumber(number: String) {
        viewModelScope.launch { _certificationNumberState.emit(number) }
    }

    val certificationNumberInfoMessageUiState =
        certificationNumberState.combine(currentCertificationNumberTimeLiveData) { number, currentTime ->
            if (currentTime == 0L) {
                CertificationNumberInfoMessageUiState.InValid
            } else if (checkSMSLiveData.value?.isSuccessful == true) {
                CertificationNumberInfoMessageUiState.Success
            } else if (number.length == 4 && checkSMSLiveData.value?.isSuccessful == false) {
                CertificationNumberInfoMessageUiState.Wrong
            } else {
                val result = convertMillisecondsToFormattedTime(currentTime).run { "${minutes}분 ${seconds}초 남음" }
                CertificationNumberInfoMessageUiState.Timer(result)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CertificationNumberInfoMessageUiState.Timer(""),
            )

    private val _nicknameState = MutableStateFlow("")
    val nicknameState = _nicknameState.asStateFlow()

    fun updateNickname(value: String) {
        _nicknameState.value = value
    }

    private val regex1 = "[0-9a-z_.]{1,20}".toRegex()
    private val startsWithDotRegex = "^[.].*\$".toRegex()
    private val onlyNumberRegex = "^[0-9]*\$".toRegex()
    private val regex7 = "^[._]*\$".toRegex()

    @OptIn(ExperimentalCoroutinesApi::class)
    val nicknameRuleState: StateFlow<NicknameRule> = nicknameState.flatMapLatest { nickname ->
        if (nickname.isEmpty()) {
            flowOf(NicknameRule.Nothing)
        } else if (!isNicknameRuleMatch(nickname, regex1)) {
            flowOf(NicknameRule.UnAvailable)
        } else if (isNicknameRuleMatch(nickname, startsWithDotRegex)) {
            flowOf(NicknameRule.StartsWithDot)
        } else if (isNicknameRuleMatch(nickname, onlyNumberRegex)) {
            flowOf(NicknameRule.OnlyNumber)
        } else if (isNicknameRuleMatch(nickname, regex7)) {
            flowOf(NicknameRule.UnAvailable)
        } else {
            withContext(Dispatchers.IO) {
                if (networkRepository.checkNickname(nickname).body()?.success == true) {
                    flowOf(NicknameRule.Available)
                } else {
                    flowOf(NicknameRule.InUsed)
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NicknameRule.Nothing
    )

    val sendSMSLiveData: LiveData<Response<SendSMSResponse>> get() =  _sendSMSLiveData
    private val _sendSMSLiveData = MutableLiveData<Response<SendSMSResponse>>()

    // TODO: debounce 적용하기 
    fun sendSMS(phoneNumber: String) {
        viewModelScope.launch(ioDispatcher) {
            val response = networkRepository.sendSMS(phoneNumber)
            _sendSMSLiveData.postValue(response)
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
    private val _genderState = MutableStateFlow<Gender?>(null)
    val genderState = _genderState.asStateFlow()

    fun updateGender(value: Gender) {
        _genderState.value = value
    }

    private val _profileImageState = MutableStateFlow<ProfileImages?>(null)
    val profileImageState = _profileImageState.asStateFlow()

    fun updateProfileImage(value: ProfileImages) {
        _profileImageState.value = value
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