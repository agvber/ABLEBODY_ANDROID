package com.smilehunter.ablebody.presentation.comment.data

import com.smilehunter.ablebody.model.CommentListData

sealed interface CommentUiState {
    object LoadFail: CommentUiState
    object Loading: CommentUiState
    data class CommentList(val data: List<CommentListData>): CommentUiState
}