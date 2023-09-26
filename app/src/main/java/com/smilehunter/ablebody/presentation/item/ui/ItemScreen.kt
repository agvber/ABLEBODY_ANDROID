package com.smilehunter.ablebody.presentation.item.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.model.ProductItemData
import com.smilehunter.ablebody.model.fake.fakeProductItemData
import com.smilehunter.ablebody.presentation.item.ItemViewModel
import com.smilehunter.ablebody.ui.product_item.ProductItemListLayout
import com.smilehunter.ablebody.ui.theme.ABLEBODY_AndroidTheme
import com.smilehunter.ablebody.ui.utils.ItemSearchBar
import kotlinx.coroutines.flow.flowOf

@Composable
fun ItemRoute(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    itemClick: (Long) -> Unit,
    itemViewModel: ItemViewModel = hiltViewModel()
) {
    ItemScreen(
        onSearchBarClick = onSearchBarClick,
        onAlertButtonClick = onAlertButtonClick,
        itemClick = itemClick,
        onSortingMethodChange = { itemViewModel.updateSortingMethod(it) },
        onParentFilterChange = { itemViewModel.updateItemParentCategory(it) },
        onChildFilterChange = { itemViewModel.updateItemChildCategory(it) },
        onGenderChange = { itemViewModel.updateItemGender(it) },
        sortingMethod = itemViewModel.sortingMethod.collectAsStateWithLifecycle().value,
        itemParentCategory = itemViewModel.itemParentCategory.collectAsStateWithLifecycle().value,
        itemChildCategory = itemViewModel.itemChildCategory.collectAsStateWithLifecycle().value,
        gender = itemViewModel.itemGender.collectAsStateWithLifecycle().value,
        productPagingItems = itemViewModel.productItemListTest.collectAsLazyPagingItems()
    )
}

@Composable
fun ItemScreen(
    onSearchBarClick: () -> Unit = {},
    onAlertButtonClick: () -> Unit = {},
    itemClick: (Long) -> Unit = {},
    onSortingMethodChange: (SortingMethod) -> Unit = {},
    onParentFilterChange: (ItemParentCategory) -> Unit = {},
    onChildFilterChange: (ItemChildCategory?) -> Unit = {},
    onGenderChange: (ItemGender) -> Unit = {},
    sortingMethod: SortingMethod = SortingMethod.POPULAR,
    itemParentCategory: ItemParentCategory = ItemParentCategory.ALL,
    itemChildCategory: ItemChildCategory? = null,
    gender: ItemGender = ItemGender.UNISEX,
    productPagingItems: LazyPagingItems<ProductItemData.Item>
) {
    Scaffold(
        topBar = {
            ItemSearchBar(
                textFiledOnClick = onSearchBarClick,
                alertOnClick = onAlertButtonClick
            )
        }
    ) { paddingValue ->
        ProductItemListLayout(
            modifier = Modifier.padding(paddingValue),
            itemClick = itemClick,
            onSortingMethodChange = onSortingMethodChange,
            onParentFilterChange = onParentFilterChange,
            onChildFilterChange = onChildFilterChange,
            onGenderChange = onGenderChange,
            sortingMethod = sortingMethod,
            itemParentCategory = itemParentCategory,
            itemChildCategory = itemChildCategory,
            gender = gender,
            productPagingItems = productPagingItems
        )
    }
}

@Preview
@Composable
fun ItemScreenPreview() {
    ABLEBODY_AndroidTheme {
        ItemScreen(productPagingItems = flowOf(PagingData.from(fakeProductItemData.content)).collectAsLazyPagingItems())
    }
}