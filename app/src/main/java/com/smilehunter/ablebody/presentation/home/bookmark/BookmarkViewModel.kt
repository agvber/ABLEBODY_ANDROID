package com.smilehunter.ablebody.presentation.home.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.smilehunter.ablebody.data.repository.BookmarkRepository
import com.smilehunter.ablebody.domain.CodyItemPagerUseCase
import com.smilehunter.ablebody.domain.CodyPagingSourceData
import com.smilehunter.ablebody.domain.ProductItemPagerUseCase
import com.smilehunter.ablebody.domain.ProductItemPagingSourceData
import com.smilehunter.ablebody.model.CodyItemData
import com.smilehunter.ablebody.model.ProductItemData
import com.smilehunter.ablebody.network.di.AbleBodyDispatcher
import com.smilehunter.ablebody.network.di.Dispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val bookmarkRepository: BookmarkRepository,
    productItemPagerUseCase: ProductItemPagerUseCase,
    codyItemPagerUseCase: CodyItemPagerUseCase
): ViewModel() {

    private val _networkRefreshFlow = MutableSharedFlow<Unit>()
    private val networkRefreshFlow = _networkRefreshFlow.asSharedFlow()

    fun refreshNetwork() {
        viewModelScope.launch { _networkRefreshFlow.emit(Unit) }
    }

    fun deleteProductItem(items: List<Long>) {
        viewModelScope.launch(ioDispatcher) {
            items.forEach { bookmarkRepository.deleteBookmarkItem(it) }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val productPagingItemList: StateFlow<PagingData<ProductItemData.Item>> =
        networkRefreshFlow.onSubscription { emit(Unit) }.flatMapLatest {
            productItemPagerUseCase(ProductItemPagingSourceData.Bookmark)
                .cachedIn(viewModelScope)
                .flowOn(ioDispatcher)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PagingData.empty()
            )


    @OptIn(ExperimentalCoroutinesApi::class)
    val codyPagingItemList: StateFlow<PagingData<CodyItemData.Item>> =
        networkRefreshFlow.onSubscription { emit(Unit) }.flatMapLatest {
            codyItemPagerUseCase(CodyPagingSourceData.Bookmark)
                .cachedIn(viewModelScope)
                .flowOn(ioDispatcher)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = PagingData.empty()
            )
}