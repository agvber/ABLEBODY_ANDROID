package com.smilehunter.ablebody.presentation.item_review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.model.ItemDetailData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ItemReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val itemReview: StateFlow<ItemDetailData.ItemReview?> =
        savedStateHandle.getStateFlow<ItemDetailData.ItemReview?>("review_data", null)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )
    }