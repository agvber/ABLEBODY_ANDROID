package com.example.ablebody_android.brand

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ablebody_android.ItemChildCategory
import com.example.ablebody_android.ItemGender
import com.example.ablebody_android.ItemParentCategory
import com.example.ablebody_android.NetworkRepository
import com.example.ablebody_android.SortingMethod
import com.example.ablebody_android.TokenSharedPreferencesRepository
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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
                        else -> it.brandGender == gender
                    }
                }
            }
                .flowOn(ioDispatcher)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )

    private val _contentID = MutableStateFlow<Long?>(null)
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

    val productItemList: StateFlow<BrandDetailItemResponseData?> =
        combine(
            brandProductItemSortingMethod,
            brandProductItemParentFilter,
            brandProductItemChildFilter,
            brandProductItemGender,
            contentID
        ) { sort, parent, child, gender, id ->
            id?.let {
                networkRepository.brandDetailItem(
                    sort = sort,
                    brandId = id,
                    itemGender = gender,
                    parentCategory = parent,
                    childCategory = child
                ).body()?.data
            }
        }
            .flowOn(ioDispatcher)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )
}