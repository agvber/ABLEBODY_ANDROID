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
import com.example.ablebody_android.retrofit.dto.response.UserDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    val isNotPhonenumberDuplicate: LiveData<Boolean> get() =  _isNotPhonenumberDuplicate
    private val _isNotPhonenumberDuplicate = MutableLiveData<Boolean>()

    fun checkDuplicatePhonenumber(phoneNumber: String) {
        viewModelScope.launch(ioDispatcher) {
            val response = networkRepository.sendSMS(phoneNumber)
            if (response.body()?.code == 200) {
                _isNotPhonenumberDuplicate.postValue(true)
            } else {
                _isNotPhonenumberDuplicate.postValue(false)
            }
        }
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
}