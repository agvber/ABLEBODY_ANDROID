package com.smilehunter.ablebody.presentation.my.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
): ViewModel() {
     fun deleteToken(){
         viewModelScope.launch {
            tokenRepository.deleteToken()
         }
     }
}