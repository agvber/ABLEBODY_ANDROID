package com.smilehunter.ablebody.presentation.main

import androidx.lifecycle.ViewModel
import com.smilehunter.ablebody.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    tokenRepository: TokenRepository
): ViewModel() {

    val responseInvalidRefreshToken = tokenRepository.registerOnClearedListener
}