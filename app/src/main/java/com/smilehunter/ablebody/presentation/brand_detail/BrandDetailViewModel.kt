package com.smilehunter.ablebody.presentation.brand_detail

import androidx.lifecycle.SavedStateHandle
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
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.domain.CodyItemPagerUseCase
import com.smilehunter.ablebody.domain.CodyPagingSourceData
import com.smilehunter.ablebody.domain.ProductItemPagerUseCase
import com.smilehunter.ablebody.domain.ProductItemPagingSourceData
import com.smilehunter.ablebody.model.CodyItemData
import com.smilehunter.ablebody.model.LocalUserInfoData
import com.smilehunter.ablebody.model.ProductItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BrandDetailViewModel @Inject constructor(
    productItemPagerUseCase: ProductItemPagerUseCase,
    codyItemPagerUseCase: CodyItemPagerUseCase,
    userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _networkRefreshFlow = MutableSharedFlow<Unit>()
    private val networkRefreshFlow = _networkRefreshFlow.asSharedFlow()

    fun refreshNetwork() {
        viewModelScope.launch { _networkRefreshFlow.emit(Unit) }
    }

    val brandName = savedStateHandle.getStateFlow("content_name", "")
    private val contentID = savedStateHandle.getStateFlow("content_id", 0L)

    private val _brandProductItemSortingMethod = MutableStateFlow(SortingMethod.POPULAR)
    val brandProductItemSortingMethod = _brandProductItemSortingMethod.asStateFlow()

    fun updateBrandProductItemOrderFilterType(sortingMethod: SortingMethod) {
        viewModelScope.launch {
            _brandProductItemSortingMethod.emit(sortingMethod)
        }
    }

    private val _brandProductItemGender = MutableStateFlow(
        when (runBlocking { userRepository.localUserInfoData.firstOrNull()?.gender }) {
            LocalUserInfoData.Gender.MALE -> ItemGender.MALE
            LocalUserInfoData.Gender.FEMALE -> ItemGender.FEMALE
            else -> ItemGender.MALE
        }
    )
    val brandProductItemGender = _brandProductItemGender.asStateFlow()

    fun updateBrandProductItemGender(gender: ItemGender) {
        viewModelScope.launch {
            _brandProductItemGender.emit(gender)
        }
    }

    private val _brandProductItemParentFilter = MutableStateFlow(ItemParentCategory.ALL)
    val brandProductItemParentFilter = _brandProductItemParentFilter.asStateFlow()

    fun updateBrandProductItemParentFilter(parentCategory: ItemParentCategory) {
        viewModelScope.launch {
            _brandProductItemParentFilter.emit(parentCategory)
            _brandProductItemChildFilter.emit(null)
        }
    }

    private val _brandProductItemChildFilter = MutableStateFlow<ItemChildCategory?>(null)
    val brandProductItemChildFilter = _brandProductItemChildFilter.asStateFlow()

    fun updateBrandProductItemChildFilter(childCategory: ItemChildCategory?) {
        viewModelScope.launch {
            _brandProductItemChildFilter.emit(childCategory)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val productItemContentList: StateFlow<PagingData<ProductItemData.Item>> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                combine(
                    brandProductItemSortingMethod,
                    contentID,
                    brandProductItemGender,
                    brandProductItemParentFilter,
                    brandProductItemChildFilter
                ) { sort, id, gender, parent, child ->
                    productItemPagerUseCase(
                        ProductItemPagingSourceData.Brand(
                            sort,
                            id,
                            gender,
                            parent,
                            child
                        )
                    )
                }
                    .flatMapLatest { it }
                    .cachedIn(viewModelScope)
            }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = PagingData.empty()
                )

    fun resetCodyItemFilter() {
        viewModelScope.launch {
            _codyItemListGenderFilter.emit(emptyList())
            _codyItemListSportFilter.emit(emptyList())
            _codyItemListPersonHeightFilter.emit(PersonHeightFilterType.ALL)
        }
    }

    private val _codyItemListGenderFilter = MutableStateFlow<List<Gender>>(listOf())
    val codyItemListGenderFilter = _codyItemListGenderFilter.asStateFlow()

    fun updateCodyItemListGendersFilter(genders: List<Gender>) {
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

    val codyPagingItem: StateFlow<PagingData<CodyItemData.Item>> =
        networkRefreshFlow.onSubscription { emit(Unit) }
            .flatMapLatest {
                combine(
                    contentID,
                    codyItemListGenderFilter,
                    codyItemListSportFilter,
                    codyItemListPersonHeightFilter
                ) { id, gender, sport, height ->
                    codyItemPagerUseCase(
                        CodyPagingSourceData.Brand(
                            id,
                            gender,
                            sport,
                            height.rangeStart,
                            height.rangeEnd
                        )
                    )
                }
                    .flatMapLatest { it }
                    .cachedIn(viewModelScope)
            }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = PagingData.empty()
                )
}