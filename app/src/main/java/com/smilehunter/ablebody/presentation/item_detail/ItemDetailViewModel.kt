package com.smilehunter.ablebody.presentation.item_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.BookmarkRepository
import com.smilehunter.ablebody.domain.GetItemOptionListUseCase
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.IO
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository,
    private val itemUseCase: GetItemOptionListUseCase,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val itemId = savedStateHandle.getStateFlow<Long?>("content_id", null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val itemDetail = itemId.flatMapLatest { id ->
        if (id == null) return@flatMapLatest flowOf(null)
        flowOf(itemUseCase(id))
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            null
        )

    fun toggleBookMark() {
        viewModelScope.launch(ioDispatcher) {
            val currentItem = itemDetail.value
            val newBookmarkedStatus = !(currentItem?.bookmarked ?: false)

            if (newBookmarkedStatus) {
                bookmarkRepository.addBookmarkItem(itemId.value!!)
            } else {
                bookmarkRepository.deleteBookmarkItem(itemId.value!!)
            }

            // 현재 아이템의 상태를 갱신하여 LiveData를 업데이트합니다.
//            _itemDetailLiveData.postValue(currentItem?.copy(bookmarked = newBookmarkedStatus))
        }
    }


}