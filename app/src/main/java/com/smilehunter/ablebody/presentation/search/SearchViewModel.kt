package com.smilehunter.ablebody.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.smilehunter.ablebody.data.dto.Gender
import com.smilehunter.ablebody.data.dto.HomeCategory
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.PersonHeightFilterType
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.repository.SearchRepository
import com.smilehunter.ablebody.data.result.Result
import com.smilehunter.ablebody.data.result.asResult
import com.smilehunter.ablebody.database.model.SearchHistoryEntity
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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @Dispatcher(AbleBodyDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
    private val searchRepository: SearchRepository,
    productItemPagerUseCase: ProductItemPagerUseCase,
    codyItemPagerUseCase: CodyItemPagerUseCase
): ViewModel() {

    private val _keyword = MutableStateFlow<String>("")
    val keyword = _keyword.asStateFlow()

    fun updateKeyword(keyword: String) {
        viewModelScope.launch {
            _keyword.emit(keyword)
        }
    }

    val recommendedKeywords: StateFlow<Result<List<String>>> = flow {
        emit(searchRepository.uniSearch("").data!!.recommendKeyword.creator)
    }
        .flowOn(ioDispatcher)
        .asResult()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Result.Loading
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchHistoryQueries: StateFlow<List<SearchHistoryEntity>> =
        searchRepository.getSearchHistoryQueries()
            .flatMapLatest {
                flowOf(it.asReversed())
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun deleteAllSearchHistory() {
        viewModelScope.launch(ioDispatcher) { searchRepository.deleteAllSearchHistory() }
    }

    private val _productItemSortingMethod = MutableStateFlow(SortingMethod.POPULAR)
    val productItemSortingMethod = _productItemSortingMethod.asStateFlow()

    fun updateProductItemSortingMethod(sortingMethod: SortingMethod) {
        viewModelScope.launch {
            _productItemSortingMethod.emit(sortingMethod)
        }
    }

    private val _productItemGender = MutableStateFlow(ItemGender.MALE)
    val productItemGender = _productItemGender.asStateFlow()

    fun updateProductItemGender(gender: ItemGender) {
        viewModelScope.launch {
            _productItemGender.emit(gender)
        }
    }

    private val _productItemParentCategory = MutableStateFlow(ItemParentCategory.ALL)
    val productItemParentCategory = _productItemParentCategory.asStateFlow()

    fun updateProductItemParentFilter(parentCategory: ItemParentCategory) {
        viewModelScope.launch {
            _productItemParentCategory.emit(parentCategory)
            _productItemChildCategory.emit(null)
        }
    }

    private val _productItemChildCategory = MutableStateFlow<ItemChildCategory?>(null)
    val productItemChildCategory = _productItemChildCategory.asStateFlow()

    fun updateBrandProductItemChildFilter(childCategory: ItemChildCategory?) {
        viewModelScope.launch {
            _productItemChildCategory.emit(childCategory)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val productPagingItemList: Flow<PagingData<ProductItemData.Item>> = combine(
        productItemSortingMethod,
        keyword.debounce(1000L),
        productItemGender,
        productItemParentCategory,
        productItemChildCategory
    ) { sort, keyword, gender, parent, child, ->
        if (keyword.isNotEmpty()) {
            productItemPagerUseCase(ProductItemPagingSourceData.Search(sort, keyword, gender, parent, child))
        } else {
            flowOf(PagingData.empty())
        }
    }
        .flatMapLatest {
            it.cachedIn(viewModelScope)
        }

    fun resetCodyItemFilter() {
        viewModelScope.launch {
            _codyItemListGenderFilter.emit(emptyList())
            _codyItemListSportFilter.emit(emptyList())
            _codyItemListPersonHeightFilter.emit(PersonHeightFilterType.ALL)
        }
    }

    private val _codyItemListGenderFilter = MutableStateFlow<List<Gender>>(listOf())
    val codyItemListGenderFilter = _codyItemListGenderFilter.asStateFlow()

    fun updateCodyItemListGenders(genders: List<Gender>) {
        viewModelScope.launch { _codyItemListGenderFilter.emit(genders) }
    }

    private val _codyItemListSportFilter = MutableStateFlow<List<HomeCategory>>(listOf())
    val codyItemListSportFilter = _codyItemListSportFilter.asStateFlow()

    fun updateCodyItemListSportFilter(sports: List<HomeCategory>) {
        viewModelScope.launch { _codyItemListSportFilter.emit(sports) }
    }

    private val _codyItemListPersonHeightFilter = MutableStateFlow(PersonHeightFilterType.ALL)
    val codyItemListPersonHeightFilter = _codyItemListPersonHeightFilter.asStateFlow()

    fun updateCodyItemListPersonHeightFilter(sports: PersonHeightFilterType) {
        viewModelScope.launch { _codyItemListPersonHeightFilter.emit(sports) }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val codyPagingItemList: Flow<PagingData<CodyItemData.Item>> =
        combine(
            keyword.debounce(1000L),
            codyItemListGenderFilter,
            codyItemListSportFilter,
            codyItemListPersonHeightFilter
        ) { keyword, gender, sport, height ->
            if (keyword.isNotEmpty()) {
                codyItemPagerUseCase(CodyPagingSourceData.Search(keyword, gender, sport, height.rangeStart, height.rangeEnd))
            } else {
                flowOf(PagingData.empty())
            }
        }
            .flatMapLatest {
                it.cachedIn(viewModelScope)
            }
}