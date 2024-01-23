package com.smilehunter.ablebody.presentation.creator_detail.data

import com.smilehunter.ablebody.model.CreatorDetailData

sealed interface CreatorDetailUiState {

    data class Success(val data: CreatorDetailData): CreatorDetailUiState

    object Loading: CreatorDetailUiState

    data class LoadFail(val t: Throwable?): CreatorDetailUiState
}