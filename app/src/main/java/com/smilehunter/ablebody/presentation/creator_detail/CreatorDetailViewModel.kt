package com.smilehunter.ablebody.presentation.creator_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.BookmarkRepository
import com.smilehunter.ablebody.data.repository.CreatorDetailRepository
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.GetCreatorDetailDataListUseCase
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import com.smilehunter.ablebody.presentation.creator_detail.data.CreatorDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatorDetailViewModel @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    getCreatorDetailDataListUseCase: GetCreatorDetailDataListUseCase,
    userRepository: UserRepository,
    private val creatorDetailRepository: CreatorDetailRepository,
    private val bookmarkRepository: BookmarkRepository
): ViewModel() {

    private val _networkRefreshFlow = MutableSharedFlow<Unit>()
    private val networkRefreshFlow = _networkRefreshFlow.asSharedFlow()

    fun refreshNetwork() {
        viewModelScope.launch { _networkRefreshFlow.emit(Unit) }
    }

    private val contentID = savedStateHandle.getStateFlow("content_id", -1L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val creatorDetailData: StateFlow<CreatorDetailUiState> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                contentID.zip(userRepository.localUserInfoData)  { id, userInfo ->
                    getCreatorDetailDataListUseCase(id, userInfo.uid)
                }
                    .flowOn(ioDispatcher)
                    .asResult()
                    .map {
                        when (it) {
                            is Result.Error -> CreatorDetailUiState.LoadFail
                            is Result.Loading -> CreatorDetailUiState.Loading
                            is Result.Success -> CreatorDetailUiState.Success(it.data)
                        }
                    }
            }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = CreatorDetailUiState.Loading
                )

    fun toggleLike(id: Long) {
        viewModelScope.launch(ioDispatcher) { creatorDetailRepository.toggleLike(id) }
    }

    fun toggleBookmark(id: Long) {
        viewModelScope.launch(ioDispatcher) { bookmarkRepository.addBookmarkCody(id) }
    }
}