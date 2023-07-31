package com.example.ablebody_android.onboarding.data

sealed interface CertificationNumberInfoMessageUiState {
    object InValid: CertificationNumberInfoMessageUiState

    object Wrong: CertificationNumberInfoMessageUiState

    data class Timer(val string: String): CertificationNumberInfoMessageUiState

    object Success: CertificationNumberInfoMessageUiState
}