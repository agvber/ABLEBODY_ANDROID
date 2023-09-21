package com.example.ablebody_android.presentation.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.domain.ProductItemPagerUseCase
import com.example.ablebody_android.domain.ProductItemPagingSourceData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    productItemPagerUseCase: ProductItemPagerUseCase
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

    private val _itemGender = MutableStateFlow(ItemGender.MALE)
    val itemGender = _itemGender.asStateFlow()

    fun updateItemGender(itemGender: ItemGender) {
        viewModelScope.launch { _itemGender.emit(itemGender) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val productItemListTest = combine(sortingMethod, itemParentCategory, itemChildCategory, itemGender) { sort, parent, child, gender ->
        productItemPagerUseCase(ProductItemPagingSourceData.Item(sort, gender, parent, child))
    }
        .flatMapLatest {
            it.cachedIn(viewModelScope)
        }
}