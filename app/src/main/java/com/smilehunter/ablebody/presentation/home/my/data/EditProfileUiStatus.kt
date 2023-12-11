package com.smilehunter.ablebody.presentation.home.my.data

sealed interface EditProfileUiStatus {
    object Loading: EditProfileUiStatus

    object Uploading: EditProfileUiStatus

    data class LoadFail(val t: Throwable?): EditProfileUiStatus

    object Success: EditProfileUiStatus
}