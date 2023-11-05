package com.smilehunter.ablebody.presentation.home.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.data.repository.UserRepository
import com.smilehunter.ablebody.domain.ProductItemPagerUseCase
import com.smilehunter.ablebody.domain.ProductItemPagingSourceData
import com.smilehunter.ablebody.model.LocalUserInfoData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(
    productItemPagerUseCase: ProductItemPagerUseCase,
    userRepository: UserRepository
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

    private val _itemGender = MutableStateFlow(
        when (runBlocking { userRepository.localUserInfoData.firstOrNull()?.gender }) {
            LocalUserInfoData.Gender.MALE -> ItemGender.MALE
            LocalUserInfoData.Gender.FEMALE -> ItemGender.FEMALE
            else -> ItemGender.MALE
        }
    )
    val itemGender = _itemGender.asStateFlow()

    fun updateItemGender(itemGender: ItemGender) {
        viewModelScope.launch { _itemGender.emit(itemGender) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val productPagingItems = combine(sortingMethod, itemParentCategory, itemChildCategory, itemGender) { sort, parent, child, gender ->
        productItemPagerUseCase(ProductItemPagingSourceData.Item(sort, gender, parent, child))
    }
        .flatMapLatest { it }
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PagingData.empty()
        )
}