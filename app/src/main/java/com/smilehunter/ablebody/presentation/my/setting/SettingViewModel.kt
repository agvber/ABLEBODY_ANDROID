package com.smilehunter.ablebody.presentation.my.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModel() {
    private val _sendErrorLiveData = MutableLiveData<Throwable?>()
    val sendErrorLiveData: LiveData<Throwable?> = _sendErrorLiveData

    fun deleteToken() {
        viewModelScope.launch {
            try {
                tokenRepository.deleteToken()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}