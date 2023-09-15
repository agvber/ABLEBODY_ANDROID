package com.example.ablebody_android.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ablebody_android.sharedPreferences.TokenSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    tokenSharedPreferences: TokenSharedPreferences,
): ViewModel() {

    init {
        tokenSharedPreferences.putAuthToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9" +
        ".eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiOTk5OTk5OSIsImV4cCI6MTc3OTkzNjE" +
        "0M30.Ewo_tMdZIksV-Y3F3jPNdeuA_4Z5N-yNTwZtF9qyIu6DC03Cga9bw6Zp7k1K2ESwmPHkxF7rWCisyp1LDYMONQ")

    }

    val hasAuthToken = tokenSharedPreferences.getAuthToken().let { !it.isNullOrBlank() }

    val responseInvalidRefreshToken = tokenSharedPreferences.registerOnClearedListener
        .distinctUntilChanged()
        .asLiveData()
}