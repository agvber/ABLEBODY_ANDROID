package com.example.ablebody_android.presentation.item

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ablebody_android.presentation.item.ui.ItemRoute


fun NavGraphBuilder.addItemGraph(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    productItemClick: (Long) -> Unit,
) {
    composable(route = "ItemRoute") {
        ItemRoute(
            onSearchBarClick = onSearchBarClick,
            onAlertButtonClick = onAlertButtonClick,
            itemClick = productItemClick
        )
    }
}