package com.smilehunter.ablebody.presentation.home.my.data

sealed interface NicknameCheckUiState {

    data class LoadFail(val t: Throwable?): NicknameCheckUiState

    object Loading: NicknameCheckUiState

    object Available: NicknameCheckUiState

    object UnAvailable: NicknameCheckUiState
}