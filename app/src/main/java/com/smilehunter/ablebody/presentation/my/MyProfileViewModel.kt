package com.smilehunter.ablebody.presentation.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
//    private val myRepository: MyRepository
): ViewModel() {
    fun getData(id: Long){
        viewModelScope.launch {
        }
    }
}