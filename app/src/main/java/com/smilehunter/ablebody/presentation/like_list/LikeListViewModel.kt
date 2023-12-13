package com.smilehunter.ablebody.presentation.like_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.GetLikeListUseCase
import com.smilehunter.ablebody.model.LikedLocations
import com.smilehunter.ablebody.presentation.like_list.data.LikeListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikeListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getLikeListUseCase: GetLikeListUseCase
): ViewModel() {

    private val _networkRefreshFlow = MutableSharedFlow<Unit>()
    private val networkRefreshFlow = _networkRefreshFlow.asSharedFlow()

    fun refreshNetwork() {
        viewModelScope.launch { _networkRefreshFlow.emit(Unit) }
    }

    private val contentID = savedStateHandle.getStateFlow("content_id", -1L)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val likedLocations = savedStateHandle.getStateFlow("like_location", "")
        .flatMapLatest { string ->
            flowOf(LikedLocations.values().first { it.name == string })
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val likeList: StateFlow<LikeListUiState> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                contentID.zip(likedLocations) { id, location ->
                    getLikeListUseCase(location, id)
                }
                    .asResult()
                    .map {
                        when (it) {
                            is Result.Error -> LikeListUiState.LoadFail(it.exception)
                            is Result.Loading -> LikeListUiState.Loading
                            is Result.Success -> LikeListUiState.LikeList(it.data)
                        }
                    }
            }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = LikeListUiState.Loading
                )
}