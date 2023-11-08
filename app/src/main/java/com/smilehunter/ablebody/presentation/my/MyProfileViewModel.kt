package com.smilehunter.ablebody.presentation.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.domain.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase
//    private val myRepository: MyRepository
): ViewModel() {
    fun getData(id: Long){
        viewModelScope.launch {
            getUserInfoUseCase()
        }
    }
}