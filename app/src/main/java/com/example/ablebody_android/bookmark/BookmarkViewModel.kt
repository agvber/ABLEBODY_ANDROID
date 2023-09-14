package com.example.ablebody_android.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ablebody_android.data.repository.BookmarkRepository
import com.example.ablebody_android.data.dto.response.data.ReadBookmarkCodyData
import com.example.ablebody_android.data.dto.response.data.ReadBookmarkItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val bookmarkRepository: BookmarkRepository
): ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    fun deleteProductItem(items: List<Long>) {
        viewModelScope.launch(ioDispatcher) {
            items.forEach { bookmarkRepository.deleteBookmarkItem(it) }
        }
    }

    private val isProductItemPageLastIndex = MutableStateFlow(false)
    private val productItemCurrentPageIndex = MutableStateFlow(0)

    fun requestProductItemPageChange() {
        if (_productItemList.value.isNotEmpty() && !isProductItemPageLastIndex.value) {
            productItemCurrentPageIndex.value += 1
        }
    }

    private val _productItemList = MutableStateFlow<List<ReadBookmarkItemData.Item>>(emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    val productItemList: StateFlow<List<ReadBookmarkItemData.Item>> =
        productItemCurrentPageIndex.flatMapLatest { page ->
            bookmarkRepository.readBookmarkItem(page = page).body()?.data
                ?.also { isProductItemPageLastIndex.emit(it.last) }
                ?.let { _productItemList.emit(_productItemList.value.toMutableList().apply { addAll(it.content) }) }
            _productItemList
        }
            .flowOn(ioDispatcher)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    private val isCodyItemPageLastIndex = MutableStateFlow(false)
    private val codyItemCurrentPageIndex = MutableStateFlow(0)

    fun requestCodyItemPageChange() {
        if (_codyItemList.value.isNotEmpty() && !isCodyItemPageLastIndex.value) {
            codyItemCurrentPageIndex.value += 1
        }
    }

    private val _codyItemList = MutableStateFlow<List<ReadBookmarkCodyData.Item>>(emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    val codyItemList: StateFlow<List<ReadBookmarkCodyData.Item>> =
        codyItemCurrentPageIndex.flatMapLatest { page ->
            bookmarkRepository.readBookmarkCody(page = page).body()?.data
                ?.also { isCodyItemPageLastIndex.emit(it.last) }
                ?.let { _codyItemList.emit(_codyItemList.value.toMutableList().apply { addAll(it.content) }) }
            _codyItemList
        }
            .flowOn(ioDispatcher)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


}