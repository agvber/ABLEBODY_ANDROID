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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
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

    private val _contentID = MutableStateFlow<Long>(-1)
    val contentID = _contentID.asStateFlow()

    fun updateContentID(id: Long) {
        viewModelScope.launch { _contentID.emit(id) }
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

    private val productItemListCurrentPage = MutableStateFlow(0)
    fun updateProductItemListCurrentPage() {
        if (productItemList.value?.last == false) {
            productItemListCurrentPage.value += 1
        }
    }

    init {
        combine(
            brandProductItemSortingMethod,
            brandProductItemParentFilter,
            brandProductItemChildFilter,
            brandProductItemGender,
            contentID,
        ) { sort, parent, child, gender, id ->
            productItemListCurrentPage.emit(0)
        }
            .launchIn(viewModelScope)
    }

    private val productItemList: StateFlow<BrandDetailItemResponseData?> =
        combine(
            brandProductItemSortingMethod,
            brandProductItemParentFilter,
            brandProductItemChildFilter,
            brandProductItemGender,
            contentID,
            productItemListCurrentPage
        ) { sort, parent, child, gender, id, page ->
            networkRepository.brandDetailItem(
                sort = sort,
                brandId = id,
                itemGender = gender,
                parentCategory = parent,
                childCategory = child,
                page = page
            ).body()?.data
        }
            .flowOn(ioDispatcher)
            .onEach { if (it?.first == true) {
                _productItemContentList.emit(emptyList()); productItemListCurrentPage.emit(0)
            } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    private val _productItemContentList = MutableStateFlow<List<BrandDetailItemResponseData.Item>>(listOf())
    val productItemContentList: StateFlow<List<BrandDetailItemResponseData.Item>> =
        productItemList.flatMapLatest { itemList ->
            val contentList = _productItemContentList.value

            flowOf(contentList.toMutableList().apply {
                addAll(itemList?.content ?: emptyList())
                _productItemContentList.emit(this)
            })
        }
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

    val codyItemList: StateFlow<BrandDetailCodyResponseData?> =
        combine(
            contentID,
            codyItemListGenderFilter,
            codyItemListSportFilter,
            codyItemListPersonHeightFilter
        ) { id, gender, sport, height ->
            networkRepository.brandDetailCody(
                brandId = id,
                gender = gender,
                category = sport,
                personHeightRangeStart = height.rangeStart,
                personHeightRangeEnd = height.rangeEnd
            ).body()?.data
        }
            .flowOn(ioDispatcher)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )
}


inline fun <T1, T2, T3, T4, T5, T6, R> combine(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6) -> R
): Flow<R> {
    return kotlinx.coroutines.flow.combine(flow, flow2, flow3, flow4, flow5, flow6) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        transform(
            args[0] as T1,
            args[1] as T2,
            args[2] as T3,
            args[3] as T4,
            args[4] as T5,
            args[5] as T6,
        )
    }
}