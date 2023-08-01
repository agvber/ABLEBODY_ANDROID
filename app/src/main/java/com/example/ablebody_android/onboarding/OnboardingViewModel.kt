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
import com.example.ablebody_android.retrofit.dto.response.AbleBodyResponse
import com.example.ablebody_android.retrofit.dto.response.SendSMSResponse
import com.example.ablebody_android.retrofit.dto.response.data.NewUserCreateResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
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

    private val phoneNumberRegex = "^01[0-1, 7][0-9]{8}\$".toRegex()
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val isPhoneNumberCorrectState: StateFlow<Boolean> =
        phoneNumberState.debounce(500L).flatMapLatest { number ->
            flowOf(number.isNotEmpty() && phoneNumberRegex.matches(number))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val phoneNumberMessageStateUi: StateFlow<String> =
        phoneNumberState.debounce(500L).flatMapLatest { number ->
            if (number.isEmpty() || phoneNumberRegex.matches(number)) {
                flowOf("")
            } else {
                flowOf("휴대폰 번호 양식에 맞지 않아요.")
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )


    private val _smsResponse = MutableLiveData<Response<SendSMSResponse>>()
    private val smsResponse: LiveData<Response<SendSMSResponse>> = _smsResponse

    private var lastSmsVerificationCodeRequestTime = 0L
    fun requestSmsVerificationCode(phoneNumber: String) {
        viewModelScope.launch(ioDispatcher) {
            if (System.currentTimeMillis() - lastSmsVerificationCodeRequestTime >= 1000L) {
                lastSmsVerificationCodeRequestTime = System.currentTimeMillis()
                val response = networkRepository.sendSMS(phoneNumber)
                _smsResponse.postValue(response)
            }
        }
    }

    private val _currentCertificationNumberTimeLiveData = MutableStateFlow(180000L)
    private val currentCertificationNumberTimeLiveData = _currentCertificationNumberTimeLiveData.asStateFlow()

    private val certificationNumberCountDownTimer = CertificationNumberCountDownTimer()

    fun startCertificationNumberTimer() {
        certificationNumberCountDownTimer.startCertificationNumberTimer()
        certificationNumberCountDownTimer.registerOnChangeListener(
            object : CertificationNumberCountDownTimer.Callback {
                override fun onChangeValue(value: Long) {
                    _currentCertificationNumberTimeLiveData.value = value
                }
            }
        )
    }

    fun cancelCertificationNumberCountDownTimer() {
        certificationNumberCountDownTimer.unRegisterOnChangeListener()
        certificationNumberCountDownTimer.cancelCertificationNumberCountDownTimer()
    }

    private val _certificationNumberState = MutableStateFlow<String>("")
    val certificationNumberState: StateFlow<String> = _certificationNumberState.asStateFlow()

    fun updateCertificationNumber(number: String) {
        viewModelScope.launch { _certificationNumberState.emit(number) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val verificationResultState = certificationNumberState.flatMapLatest { number ->
        val phoneConfirmID = smsResponse.value?.body()?.data?.phoneConfirmId
        if (number.length == 4 && phoneConfirmID != null) {
            withContext(Dispatchers.IO) {
                flowOf(networkRepository.checkSMS(phoneConfirmID, number).isSuccessful)
            }
        } else {
            flowOf(false)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val certificationNumberInfoMessageUiState = currentCertificationNumberTimeLiveData.flatMapLatest { time ->
        if (time == 0L) {
            flowOf(CertificationNumberInfoMessageUiState.Timeout)
        } else {
            certificationNumberState.flatMapLatest { number ->
                if (number.length == 4) {
                    verificationResultState.debounce(100L).flatMapLatest { success ->
                        if (success) { flowOf(CertificationNumberInfoMessageUiState.Success) }
                        else { flowOf(CertificationNumberInfoMessageUiState.InValid) }
                    }
                } else {
                    convertMillisecondsToFormattedTime(time)
                        .run { "${minutes}분 ${seconds}초 남음" }
                        .let { CertificationNumberInfoMessageUiState.Timer(it) }
                        .let { flowOf(it) }
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = CertificationNumberInfoMessageUiState.Timer("")
    )

    private val _nicknameState = MutableStateFlow("")
    val nicknameState = _nicknameState.asStateFlow()

    fun updateNickname(value: String) { _nicknameState.value = value }

    private val regex1 = "[0-9a-z_.]{1,20}".toRegex()
    private val startsWithDotRegex = "^[.].*\$".toRegex()
    private val onlyNumberRegex = "^[0-9]*\$".toRegex()
    private val regex7 = "^[._]*\$".toRegex()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val nicknameRuleState: StateFlow<NicknameRule> =
        nicknameState.debounce(100L).flatMapLatest { nickname ->
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

    private val _genderState = MutableStateFlow<Gender?>(null)
    val genderState = _genderState.asStateFlow()

    fun updateGender(value: Gender) { _genderState.value = value }

    private val _profileImageState = MutableStateFlow<ProfileImages?>(null)
    val profileImageState = _profileImageState.asStateFlow()

    fun updateProfileImage(value: ProfileImages) { _profileImageState.value = value }

    private val _createNewUser = MutableSharedFlow<Response<AbleBodyResponse<NewUserCreateResponseData>>>()
    val createNewUser: SharedFlow<Response<AbleBodyResponse<NewUserCreateResponseData>>> = _createNewUser
    fun createNewUser(
        gender: Gender,
        nickname: String,
        profileImage: Int,
        verifyingCode: String,
        agreeRequiredConsent: Boolean,
        agreeMarketingConsent: Boolean
    ) {
        viewModelScope.launch(ioDispatcher) {
            networkRepository.createNewUser(
                gender = gender,
                nickname = nickname,
                profileImage = profileImage,
                verifyingCode = verifyingCode,
                agreeRequiredConsent = agreeRequiredConsent,
                agreeMarketingConsent = agreeMarketingConsent
            ).let { _createNewUser.emit(it) }
        }
    }

    fun putAuthToken(token: String) {
        viewModelScope.launch(ioDispatcher) {
            tokenSharedPreferencesRepository.putAuthToken(token)
        }
    }

    fun getAuthToken() = tokenSharedPreferencesRepository.getAuthToken()
}