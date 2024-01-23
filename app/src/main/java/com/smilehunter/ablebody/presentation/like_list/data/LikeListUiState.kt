package com.smilehunter.ablebody.presentation.like_list.data

import com.smilehunter.ablebody.model.LikeListData

sealed interface LikeListUiState {

    object Loading: LikeListUiState

    data class LoadFail(val t: Throwable?): LikeListUiState

    data class LikeList(val data: List<LikeListData>): LikeListUiState

}