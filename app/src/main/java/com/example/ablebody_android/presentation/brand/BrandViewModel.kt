package com.example.ablebody_android.presentation.brand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.ablebody_android.data.dto.Gender
import com.example.ablebody_android.data.dto.HomeCategory
import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.PersonHeightFilterType
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.data.dto.response.data.BrandDetailCodyResponseData
import com.example.ablebody_android.data.repository.BrandRepository
import com.example.ablebody_android.domain.ProductItemAutoPagerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BrandViewModel @Inject constructor(
    brandRepository: BrandRepository,
    productItemAutoPagerUseCase: ProductItemAutoPagerUseCase
): ViewModel() {

    private val ioDispatcher = Dispatchers.IO

    private val _brandListSortingMethod = MutableStateFlow(SortingMethod.POPULAR)
    val brandListSortingMethod = _brandListSortingMethod.asStateFlow()

    fun updateBrandListOrderFilterType(sortingMethod: SortingMethod) {
        viewModelScope.launch {
            _brandListSortingMethod.emit(sortingMethod)
        }
    }

    private val _brandListGenderFilterType = MutableStateFlow(ItemGender.UNISEX)
    val brandListGenderFilterType = _brandListGenderFilterType.asStateFlow()

    fun updateBrandListGenderFilterType(gender: ItemGender) {
        viewModelScope.launch {
            _brandListGenderFilterType.emit(gender)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val brandItemList: StateFlow<List<com.example.ablebody_android.data.dto.response.data.BrandMainResponseData>> =
        brandListSortingMethod.flatMapLatest { sortingMethod ->
            flowOf(brandRepository.brandMain(sortingMethod).body()?.data ?: emptyList())
        }
            .combine(brandListGenderFilterType) { data, gender ->
                data.filter {
                    when (gender) {
                        ItemGender.UNISEX -> true
                        else -> it.brandGender == ItemGender.UNISEX || it.brandGender == gender
                    }
                }
            }
                .flowOn(ioDispatcher)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )

    private val contentID = MutableStateFlow<Long>(-1)

    fun updateContentID(id: Long) {
        viewModelScope.launch { contentID.emit(id) }
    }

    private val _brandProductItemSortingMethod = MutableStateFlow(SortingMethod.POPULAR)
    val brandProductItemSortingMethod = _brandProductItemSortingMethod.asStateFlow()

    fun updateBrandProductItemOrderFilterType(sortingMethod: SortingMethod) {
        viewModelScope.launch {
            _brandProductItemSortingMethod.emit(sortingMethod)
        }
    }

    private val _brandProductItemGender = MutableStateFlow(ItemGender.MALE)
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
    val productItemContentList = combine(
        brandProductItemSortingMethod,
        contentID,
        brandProductItemGender,
        brandProductItemParentFilter,
        brandProductItemChildFilter
    ) { sort, id, gender, parent, child, ->
        productItemAutoPagerUseCase(sort, id, gender, parent, child)
    }
        .flatMapLatest {
            it.cachedIn(viewModelScope)
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

    fun resetCodyItemListFilter() {
        viewModelScope.launch {
            _codyItemListGenderFilter.emit(listOf())
            _codyItemListSportFilter.emit(listOf())
            _codyItemListPersonHeightFilter.emit(PersonHeightFilterType.ALL)
        }
    }

    private val isCodyItemListPageLastIndex = MutableStateFlow(false)
    private val codyItemListCurrentPageIndex = MutableStateFlow(0)
    fun requestCodyItemPageChange() {
        if (!isCodyItemListPageLastIndex.value) {
            codyItemListCurrentPageIndex.value += 1
        }
    }

    private val _codyItemContentList = MutableStateFlow<List<BrandDetailCodyResponseData.Item>>(emptyList())
    val codyItemContentList: StateFlow<List<BrandDetailCodyResponseData.Item>> =
        combine(
            contentID,
            codyItemListGenderFilter,
            codyItemListSportFilter,
            codyItemListPersonHeightFilter
        ) { id, gender, sport, height ->
            _codyItemContentList.emit(emptyList())
            codyItemListCurrentPageIndex.emit(0)
            arrayOf(id, gender, sport, height)
        }
            .combine(codyItemListCurrentPageIndex) { requestDataList, page ->
                brandRepository.brandDetailCody(
                    brandId = requestDataList[0] as Long,
                    gender = requestDataList[1] as List<Gender>,
                    category = requestDataList[2] as List<HomeCategory>,
                    personHeightRangeStart = (requestDataList[3] as PersonHeightFilterType).rangeStart,
                    personHeightRangeEnd = (requestDataList[3] as PersonHeightFilterType).rangeEnd,
                    page = page
                ).body()?.data
            }
                .onEach {
                    if (it != null) {
                        isCodyItemListPageLastIndex.emit(it.last)
                        _codyItemContentList.emit(_codyItemContentList.value.toMutableList().apply { addAll(it.content) })
                    }
                }
                    .flatMapLatest {
                        _codyItemContentList
                    }
                        .flowOn(ioDispatcher)
                        .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5_000),
                            initialValue = emptyList()
                        )
}