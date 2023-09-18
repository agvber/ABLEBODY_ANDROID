package com.example.ablebody_android.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ablebody_android.sharedPreferences.TokenSharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    tokenSharedPreferences: TokenSharedPreferences,
): ViewModel() {

    val responseInvalidRefreshToken = tokenSharedPreferences.registerOnClearedListener
        .distinctUntilChanged()
        .onStart {
            if (tokenSharedPreferences.getAuthToken().let { it.isNullOrBlank() }) {
               emit(Unit)
            }
        }
        .asLiveData()
}