package com.smilehunter.ablebody.presentation.comment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.CommentRepository
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.GetCommentListUseCase
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import com.smilehunter.ablebody.presentation.comment.data.CommentUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    getCommentListUseCase: GetCommentListUseCase,
    userRepository: UserRepository,
    private val commentRepository: CommentRepository,
): ViewModel() {

    private val _networkRefreshFlow = MutableSharedFlow<Unit>()
    private val networkRefreshFlow = _networkRefreshFlow.asSharedFlow()

    fun refreshNetwork() {
        viewModelScope.launch { _networkRefreshFlow.emit(Unit) }
    }

    private val contentID = savedStateHandle.getStateFlow("content_id", -1L)

    private val renewData = MutableSharedFlow<Unit>()

    val myUserInfoData = userRepository.localUserInfoData
        .flowOn(ioDispatcher)
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            replay = 1
        )


    @OptIn(ExperimentalCoroutinesApi::class)
    val commentListData: StateFlow<CommentUiState> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                combine(contentID, myUserInfoData, renewData.onSubscription { emit(Unit) }) { id, myData, _ ->
                    getCommentListUseCase(id, myData.uid)
                }
                    .flowOn(ioDispatcher)
                    .asResult()
                    .map {
                        when (it) {
                            is Result.Error -> CommentUiState.LoadFail(it.exception)
                            is Result.Loading -> CommentUiState.Loading
                            is Result.Success -> CommentUiState.CommentList(it.data)
                        }
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
            renewData.emit(Unit)
        }
    }

    fun updateReplyText(id: Long, text: String) {
        viewModelScope.launch {
            try {
                commentRepository.creatorDetailReply(id, text)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            renewData.emit(Unit)
        }
    }

    fun deleteComment(id: Long) {
        viewModelScope.launch {
            try {
                commentRepository.creatorDetailDeleteComment(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            renewData.emit(Unit)
        }
    }

    fun deleteReply(id: Long) {
        viewModelScope.launch {
            try {
                commentRepository.creatorDetailDeleteReply(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            renewData.emit(Unit)
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