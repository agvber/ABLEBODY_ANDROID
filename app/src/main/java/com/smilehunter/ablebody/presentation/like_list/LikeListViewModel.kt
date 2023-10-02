package com.smilehunter.ablebody.presentation.like_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.domain.GetLikeListUseCase
import com.smilehunter.ablebody.model.LikeListData
import com.smilehunter.ablebody.model.LikedLocations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LikeListViewModel @Inject constructor(
    private val getLikeListUseCase: GetLikeListUseCase
): ViewModel() {

    private val contentID = MutableSharedFlow<Long>()

    fun updateContentID(id: Long) {
        viewModelScope.launch { contentID.emit(id) }
    }


    private val _likedLocations = MutableStateFlow(LikedLocations.BOARD)
    val likeLocations = _likedLocations.asStateFlow()

    val likeList: StateFlow<List<LikeListData>> =
        contentID.zip(likeLocations) { id, location ->
            getLikeListUseCase(location, id)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}