package com.example.ablebody_android.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ablebody_android.NetworkRepository
import com.example.ablebody_android.TokenSharedPreferencesRepository
import com.example.ablebody_android.retrofit.dto.response.UserDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnboardingViewModel(application: Application): AndroidViewModel(application) {

    private val tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(application.applicationContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val ioDispatcher = Dispatchers.IO

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