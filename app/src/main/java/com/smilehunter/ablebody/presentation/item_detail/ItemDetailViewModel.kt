package com.smilehunter.ablebody.presentation.item_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smilehunter.ablebody.data.repository.BookmarkRepository
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.domain.GetItemOptionListUseCase
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher.IO
import com.smilehunter.ablebody.network.di.Dispatcher
import com.smilehunter.ablebody.presentation.item_detail.data.ItemDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onSubscription
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

    private val _networkRefreshFlow = MutableSharedFlow<Unit>()
    private val networkRefreshFlow = _networkRefreshFlow.asSharedFlow()

    fun refreshNetwork() {
        viewModelScope.launch { _networkRefreshFlow.emit(Unit) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val itemDetail: StateFlow<ItemDetailUiState> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                itemId.flatMapLatest { id ->
                    if (id == null) {
                        return@flatMapLatest emptyFlow()
                    }
                    flowOf(itemUseCase(id))
                }
                    .asResult()
                    .map {
                        when (it) {
                            is Result.Error -> ItemDetailUiState.Error(it.exception)
                            is Result.Loading -> ItemDetailUiState.Loading
                            is Result.Success -> ItemDetailUiState.Success(it.data)
                        }
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ItemDetailUiState.Loading
            )

    fun toggleBookmarkItem(enable: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            if (enable) {
                bookmarkRepository.addBookmarkItem(itemId.value!!)
            } else {
                bookmarkRepository.deleteBookmarkItem(itemId.value!!)
            }
        }
    }


}