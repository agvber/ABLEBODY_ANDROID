package com.example.ablebody_android.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.ablebody_android.AbleBodyApplication
import com.example.ablebody_android.NetworkRepository
import com.example.ablebody_android.retrofit.dto.response.data.ReadBookmarkCodyData
import com.example.ablebody_android.retrofit.dto.response.data.ReadBookmarkItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val networkRepository: NetworkRepository
): ViewModel() {

    companion object {
        val Factory : ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return BookmarkViewModel(
                    networkRepository = (application as AbleBodyApplication).networkRepository
                ) as T
            }
        }
    }

    private val ioDispatcher = Dispatchers.IO

    fun deleteProductItem(items: List<Long>) {
        viewModelScope.launch(ioDispatcher) {
            items.forEach { networkRepository.deleteBookmarkItem(it) }
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
            networkRepository.readBookmarkItem(page = page).body()?.data
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
            networkRepository.readBookmarkCody(page = page).body()?.data
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