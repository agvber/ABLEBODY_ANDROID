package com.example.ablebody_android.item.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ablebody_android.data.dto.ItemChildCategory
import com.example.ablebody_android.data.dto.ItemGender
import com.example.ablebody_android.data.dto.ItemParentCategory
import com.example.ablebody_android.data.dto.SortingMethod
import com.example.ablebody_android.item.ItemViewModel
import com.example.ablebody_android.model.ProductItemData
import com.example.ablebody_android.ui.product_item.ProductItemListLayout
import com.example.ablebody_android.ui.theme.ABLEBODY_AndroidTheme

@Composable
fun ItemRoute(itemViewModel: ItemViewModel = hiltViewModel()) {
    ItemScreen(
        itemClick = { /* TODO 아이템 버튼 클릭 */ },
        requestNextPage = { itemViewModel.requestNextPage() },
        productContentItem = itemViewModel.productItemList.collectAsStateWithLifecycle().value,
        onSortingMethodChange = { itemViewModel.updateSortingMethod(it) },
        onParentFilterChange = { itemViewModel.updateItemParentCategory(it) },
        onChildFilterChange = { itemViewModel.updateItemChildCategory(it) },
        onGenderChange = { itemViewModel.updateItemGender(it) },
        sortingMethod = itemViewModel.sortingMethod.collectAsStateWithLifecycle().value,
        itemParentCategory = itemViewModel.itemParentCategory.collectAsStateWithLifecycle().value,
        itemChildCategory = itemViewModel.itemChildCategory.collectAsStateWithLifecycle().value,
        gender = itemViewModel.itemGender.collectAsStateWithLifecycle().value
    )
}

@Composable
fun ItemScreen(
    itemClick: (Long) -> Unit = {},
    requestNextPage: () -> Unit = {},
    productContentItem: List<ProductItemData.Item> = emptyList(),
    onSortingMethodChange: (SortingMethod) -> Unit = {},
    onParentFilterChange: (ItemParentCategory) -> Unit = {},
    onChildFilterChange: (ItemChildCategory?) -> Unit = {},
    onGenderChange: (ItemGender) -> Unit = {},
    sortingMethod: SortingMethod = SortingMethod.POPULAR,
    itemParentCategory: ItemParentCategory = ItemParentCategory.ALL,
    itemChildCategory: ItemChildCategory? = null,
    gender: ItemGender = ItemGender.UNISEX
) {
    ProductItemListLayout(
        itemClick = itemClick,
        requestNextPage = requestNextPage,
        productContentItem = productContentItem,
        onSortingMethodChange = onSortingMethodChange,
        onParentFilterChange = onParentFilterChange,
        onChildFilterChange = onChildFilterChange,
        onGenderChange = onGenderChange,
        sortingMethod = sortingMethod,
        itemParentCategory = itemParentCategory,
        itemChildCategory = itemChildCategory,
        gender = gender
    )
}

@Preview
@Composable
fun ItemScreenPreview() {
    ABLEBODY_AndroidTheme {
        ItemScreen()
    }
}