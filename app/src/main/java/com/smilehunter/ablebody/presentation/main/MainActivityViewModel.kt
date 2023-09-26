package com.smilehunter.ablebody.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.smilehunter.ablebody.sharedPreferences.TokenSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    tokenSharedPreferences: TokenSharedPreferences,
): ViewModel() {

    init {
        tokenSharedPreferences.putAuthToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiOTk5OTk5OSIsImV4cCI6MTc3OTkzNjE0M30.Ewo_tMdZIksV-Y3F3jPNdeuA_4Z5N-yNTwZtF9qyIu6DC03Cga9bw6Zp7k1K2ESwmPHkxF7rWCisyp1LDYMONQ")
    }

    val responseInvalidRefreshToken = tokenSharedPreferences.registerOnClearedListener
        .distinctUntilChanged()
        .onStart {
            if (tokenSharedPreferences.getAuthToken().let { it.isNullOrBlank() }) {
               emit(Unit)
            }
        }
        .asLiveData()
}