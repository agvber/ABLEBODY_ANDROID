package com.smilehunter.ablebody.presentation.like_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.domain.GetLikeListUseCase
import com.smilehunter.ablebody.model.LikeListData
import com.smilehunter.ablebody.model.LikedLocations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

@HiltViewModel
class LikeListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getLikeListUseCase: GetLikeListUseCase
): ViewModel() {

    private val contentID = savedStateHandle.getStateFlow("content_id", -1L)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val likedLocations = savedStateHandle.getStateFlow("like_location", "")
        .flatMapLatest { string ->
            flowOf(LikedLocations.values().first { it.name == string })
        }

    val likeList: StateFlow<List<LikeListData>> =
        contentID.zip(likedLocations) { id, location ->
            getLikeListUseCase(location, id)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}