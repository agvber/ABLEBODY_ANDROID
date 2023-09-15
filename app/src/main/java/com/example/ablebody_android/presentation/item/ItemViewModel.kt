package com.example.ablebody_android.presentation.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.domain.ItemUseCase
import com.example.ablebody_android.model.ProductItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    itemUseCase: ItemUseCase
): ViewModel() {

    private val _sortingMethod = MutableStateFlow(SortingMethod.POPULAR)
    val sortingMethod = _sortingMethod.asStateFlow()

    fun updateSortingMethod(sortingMethod: SortingMethod) {
       viewModelScope.launch { _sortingMethod.emit(sortingMethod) }
    }

    private val _itemParentCategory = MutableStateFlow(ItemParentCategory.ALL)
    val itemParentCategory = _itemParentCategory.asStateFlow()

    fun updateItemParentCategory(itemParentCategory: ItemParentCategory) {
        viewModelScope.launch {
            _itemParentCategory.emit(itemParentCategory)
            _itemChildCategory.emit(null)
        }
    }

    private val _itemChildCategory = MutableStateFlow<ItemChildCategory?>(null)
    val itemChildCategory = _itemChildCategory.asStateFlow()

    fun updateItemChildCategory(itemChildCategory: ItemChildCategory?) {
        viewModelScope.launch { _itemChildCategory.emit(itemChildCategory) }
    }

    private val _itemGender = MutableStateFlow(ItemGender.UNISEX)
    val itemGender = _itemGender.asStateFlow()

    fun updateItemGender(itemGender: ItemGender) {
        viewModelScope.launch { _itemGender.emit(itemGender) }
    }

    private val currentPageIndex = MutableStateFlow(0)
    private val isCurrentPageLastIndex = MutableStateFlow(false)

    fun requestNextPage() {
        if (_productItemList.value.isNotEmpty() && !isCurrentPageLastIndex.value) {
            currentPageIndex.value += 1
        }
    }

    private val _productItemList = MutableStateFlow<List<ProductItemData.Item>>(emptyList())
    @OptIn(ExperimentalCoroutinesApi::class)
    val productItemList: StateFlow<List<ProductItemData.Item>> =
        combine(sortingMethod, itemParentCategory, itemChildCategory, itemGender) { sort, parent, child, gender ->
            _productItemList.emit(emptyList())
            currentPageIndex.emit(0)
            arrayOf(sort, gender, parent, child)
        }
            .combine(currentPageIndex) { dataList, page ->
                itemUseCase(
                    dataList[0] as SortingMethod,
                    dataList[1] as ItemGender,
                    dataList[2] as ItemParentCategory,
                    dataList[3] as? ItemChildCategory,
                    page = page
                )
                    .also { isCurrentPageLastIndex.emit(it.last) }
                    .content
            }
                .flatMapLatest {
                    _productItemList.apply { emit(_productItemList.value.toMutableList().apply { addAll(it) }) }
                }
                    .flowOn(Dispatchers.IO)
                    .stateIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(5_000),
                        initialValue = emptyList()
                    )
}