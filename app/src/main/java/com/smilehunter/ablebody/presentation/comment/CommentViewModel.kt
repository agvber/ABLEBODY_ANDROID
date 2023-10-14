package com.smilehunter.ablebody.presentation.comment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.CommentRepository
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.GetCommentListUseCase
import com.smilehunter.ablebody.domain.GetUserInfoUseCase
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import com.smilehunter.ablebody.presentation.comment.data.CommentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    getCommentListUseCase: GetCommentListUseCase,
    getUserInfoUseCase: GetUserInfoUseCase,
    private val commentRepository: CommentRepository
): ViewModel() {

    private val contentID = MutableStateFlow(0L)

    private val renewData = MutableStateFlow(0)

    fun updateContentID(id: Long) {
        viewModelScope.launch { contentID.emit(id) }
    }

    val myUserInfoData = flow { emit(getUserInfoUseCase()) }
        .flowOn(ioDispatcher)
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            replay = 1
        )


    val commentListData: StateFlow<CommentUiState> =
        combine(contentID, myUserInfoData, renewData) { id, myData, _ ->
            getCommentListUseCase(id, myData.uid)
        }
            .flowOn(ioDispatcher)
            .asResult()
            .map {
                when (it) {
                    is Result.Error -> CommentUiState.LoadFail
                    is Result.Loading -> CommentUiState.Loading
                    is Result.Success -> CommentUiState.CommentList(it.data)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = CommentUiState.Loading
            )

    fun updateCommentText(text: String) {
        viewModelScope.launch {
            try {
                commentRepository.creatorDetailComment(contentID.value, text)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            renewData.emit(renewData.value + 1)
        }
    }

    fun updateReplyText(id: Long, text: String) {
        viewModelScope.launch {
            try {
                commentRepository.creatorDetailReply(id, text)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            renewData.emit(renewData.value + 1)
        }
    }

    fun deleteComment(id: Long) {
        viewModelScope.launch {
            try {
                commentRepository.creatorDetailDeleteComment(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            renewData.emit(renewData.value + 1)
        }
    }

    fun deleteReply(id: Long) {
        viewModelScope.launch {
            try {
                commentRepository.creatorDetailDeleteReply(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            renewData.emit(renewData.value + 1)
        }
    }

    fun toggleLikeComment(id: Long) {
        viewModelScope.launch {
            try {
                commentRepository.creatorDetailLikeComment(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleLikeReply(id: Long) {
        viewModelScope.launch {
            try {
                commentRepository.creatorDetailLikeReply(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}