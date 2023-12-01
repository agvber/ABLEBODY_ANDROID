package com.smilehunter.ablebody.presentation.search.data

sealed interface KeywordUiState {

    object Loading: KeywordUiState

    data class LoadFail(val t: Throwable?): KeywordUiState

    data class RecommendKeyword(val data: List<String>): KeywordUiState
}