package com.smilehunter.ablebody.presentation.home.item.ui

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.smilehunter.ablebody.data.dto.ItemChildCategory
import com.smilehunter.ablebody.data.dto.ItemGender
import com.smilehunter.ablebody.data.dto.ItemParentCategory
import com.smilehunter.ablebody.data.dto.SortingMethod
import com.smilehunter.ablebody.model.ProductItemData
import com.smilehunter.ablebody.model.fake.fakeProductItemData
import com.smilehunter.ablebody.presentation.home.item.ItemViewModel
import com.smilehunter.ablebody.presentation.main.ui.LocalNetworkConnectState
import com.smilehunter.ablebody.presentation.main.ui.error_handling.NetworkConnectionErrorDialog
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
    val sortingMethod by itemViewModel.sortingMethod.collectAsStateWithLifecycle()
    val itemParentCategory by itemViewModel.itemParentCategory.collectAsStateWithLifecycle()
    val itemChildCategory by itemViewModel.itemChildCategory.collectAsStateWithLifecycle()
    val gender by itemViewModel.itemGender.collectAsStateWithLifecycle()
    val productPagingItems = itemViewModel.productPagingItems.collectAsLazyPagingItems()
    ItemScreen(
        onSearchBarClick = onSearchBarClick,
        onAlertButtonClick = onAlertButtonClick,
        itemClick = itemClick,
        onSortingMethodChange = { itemViewModel.updateSortingMethod(it) },
        onParentFilterChange = { itemViewModel.updateItemParentCategory(it) },
        onChildFilterChange = { itemViewModel.updateItemChildCategory(it) },
        onGenderChange = { itemViewModel.updateItemGender(it) },
        sortingMethod = sortingMethod,
        itemParentCategory = itemParentCategory,
        itemChildCategory = itemChildCategory,
        gender = gender,
        productPagingItems = productPagingItems
    )

    val isNetworkDisconnected = productPagingItems.loadState.refresh is LoadState.Error || !LocalNetworkConnectState.current
    if (isNetworkDisconnected) {
        val context = LocalContext.current
        NetworkConnectionErrorDialog(
            onDismissRequest = {  },
            positiveButtonOnClick = { itemViewModel.refreshNetwork() },
            negativeButtonOnClick = {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                ContextCompat.startActivity(context, intent, null)
            }
        )
    }
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