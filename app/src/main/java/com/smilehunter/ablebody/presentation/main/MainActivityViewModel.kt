package com.smilehunter.ablebody.presentation.main

import androidx.lifecycle.ViewModel
import com.smilehunter.ablebody.data.repository.TokenRepository
import com.smilehunter.ablebody.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    tokenRepository: TokenRepository,
    userRepository: UserRepository
): ViewModel() {

    val hasToken = tokenRepository.hasToken

    val responseInvalidRefreshToken = tokenRepository.registerOnClearedListener

    val user = userRepository.localUserInfoData
}