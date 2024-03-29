package com.smilehunter.ablebody.presentation.onboarding.data

sealed interface CertificationNumberInfoMessageUiState {
    object Timeout: CertificationNumberInfoMessageUiState

    object InValid: CertificationNumberInfoMessageUiState

    data class Timer(val string: String): CertificationNumberInfoMessageUiState

    object Success: CertificationNumberInfoMessageUiState

    object Already: CertificationNumberInfoMessageUiState
}