package com.example.ablebody_android.brand

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ablebody_android.NetworkRepository
import com.example.ablebody_android.SortingMethod
import com.example.ablebody_android.TokenSharedPreferencesRepository
import com.example.ablebody_android.retrofit.dto.response.BrandMainResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class BrandViewModel(application: Application): AndroidViewModel(application) {

    private val tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(application.applicationContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val ioDispatcher = Dispatchers.IO

    init {
        tokenSharedPreferencesRepository.putAuthToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9" +
                ".eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiOTk5OTk5OSIsImV4cCI6MTc3OTkzNjE" +
                "0M30.Ewo_tMdZIksV-Y3F3jPNdeuA_4Z5N-yNTwZtF9qyIu6DC03Cga9bw6Zp7k1K2ESwmPHkxF7rWCisyp1LDYMONQ")
    }

    private val _brandMain = MutableLiveData<Response<BrandMainResponse>>()
    val brandMain: LiveData<Response<BrandMainResponse>> get() = _brandMain

    fun brandMain(
        sort: SortingMethod
    ) {
        viewModelScope.launch(ioDispatcher) {
            _brandMain.postValue(networkRepository.brandMain(sort))
        }
    }


}