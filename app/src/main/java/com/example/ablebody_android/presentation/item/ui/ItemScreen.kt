package com.example.ablebody_android.presentation.item.ui

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
import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.model.ProductItemData
import com.example.ablebody_android.model.fake.fakeProductItemData
import com.example.ablebody_android.presentation.item.ItemViewModel
import com.example.ablebody_android.ui.product_item.ProductItemListLayout
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme
import com.example.ablebody_android.ui.utils.ItemSearchBar
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