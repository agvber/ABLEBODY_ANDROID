package com.example.ablebody_android.brand

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ablebody_android.Gender
import com.example.ablebody_android.HomeCategory
import com.example.ablebody_android.ItemChildCategory
import com.example.ablebody_android.ItemGender
import com.example.ablebody_android.ItemParentCategory
import com.example.ablebody_android.NetworkRepository
import com.example.ablebody_android.PersonHeightFilterType
import com.example.ablebody_android.SortingMethod
import com.example.ablebody_android.TokenSharedPreferencesRepository
import com.example.ablebody_android.retrofit.dto.response.data.BrandDetailCodyResponseData
import com.example.ablebody_android.retrofit.dto.response.data.BrandDetailItemResponseData
import com.example.ablebody_android.retrofit.dto.response.data.BrandMainResponseData
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

@OptIn(ExperimentalCoroutinesApi::class)
class BrandViewModel(application: Application): AndroidViewModel(application) {

    private val tokenSharedPreferencesRepository = TokenSharedPreferencesRepository(application.applicationContext)
    private val networkRepository = NetworkRepository(tokenSharedPreferencesRepository)

    private val ioDispatcher = Dispatchers.IO

    init {
        tokenSharedPreferencesRepository.putAuthToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9" +
                ".eyJzdWIiOiJhdXRoLXRva2VuIiwidWlkIjoiOTk5OTk5OSIsImV4cCI6MTc3OTkzNjE" +
                "0M30.Ewo_tMdZIksV-Y3F3jPNdeuA_4Z5N-yNTwZtF9qyIu6DC03Cga9bw6Zp7k1K2ESwmPHkxF7rWCisyp1LDYMONQ")
    }

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
    val brandItemList: StateFlow<List<BrandMainResponseData>> =
        brandListSortingMethod.flatMapLatest { sortingMethod ->
            flowOf(networkRepository.brandMain(sortingMethod).body()?.data ?: emptyList())
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val brandProductItemChildCategory: StateFlow<List<ItemChildCategory>> =
        brandProductItemParentFilter.flatMapLatest { parentCategory ->
            flowOf(
                ItemChildCategory.values().filter {
                    when (parentCategory) {
                        ItemParentCategory.ALL -> true
                        else -> it.parentCategory == parentCategory
                    }
                }
            )
        }
            .combine(brandProductItemGender) { parentCategory, gender ->
                parentCategory.filter {
                    when (gender) {
                        ItemGender.UNISEX -> true
                        else -> it.gender != gender
                    }
                }
            }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = ItemChildCategory.values().toList()
                )


    private val _brandProductItemChildFilter = MutableStateFlow<ItemChildCategory?>(null)
    val brandProductItemChildFilter = _brandProductItemChildFilter.asStateFlow()

    fun updateBrandProductItemChildFilter(childCategory: ItemChildCategory?) {
        viewModelScope.launch {
            _brandProductItemChildFilter.emit(childCategory)
        }
    }

    private val isProductItemListCurrentPageLastIndex = MutableStateFlow(false)
    private val productItemListCurrentPageIndex = MutableStateFlow(0)
    fun requestProductItemListPage() {
        if (!isProductItemListCurrentPageLastIndex.value) {
            productItemListCurrentPageIndex.value += 1
        }
    }

    private val _productItemContentList = MutableStateFlow<List<BrandDetailItemResponseData.Item>>(emptyList())
    val productItemContentList: StateFlow<List<BrandDetailItemResponseData.Item>> =
        combine(
            brandProductItemSortingMethod,
            contentID,
            brandProductItemGender,
            brandProductItemParentFilter,
            brandProductItemChildFilter,
        ) { sort, id, gender, parent, child, ->
            _productItemContentList.emit(emptyList())
            productItemListCurrentPageIndex.emit(0)
            arrayOf(sort, id, gender, parent, child)
        }
            .combine(productItemListCurrentPageIndex) { requestDataList, page ->
                networkRepository.brandDetailItem(
                    sort = requestDataList[0] as SortingMethod,
                    brandId = requestDataList[1] as Long,
                    itemGender = requestDataList[2] as ItemGender,
                    parentCategory = requestDataList[3] as ItemParentCategory,
                    childCategory = requestDataList[4] as? ItemChildCategory,
                    page = page
                ).body()?.data
            }
                .onEach {
                    if (it != null) {
                        isProductItemListCurrentPageLastIndex.emit(it.last)
                        _productItemContentList.emit(_productItemContentList.value.toMutableList().apply { addAll(it.content) })
                    }
                }
                    .flatMapLatest {
                        _productItemContentList
                    }
                        .flowOn(ioDispatcher)
                        .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileSubscribed(5_000),
                            initialValue = emptyList()
                        )

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
                networkRepository.brandDetailCody(
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