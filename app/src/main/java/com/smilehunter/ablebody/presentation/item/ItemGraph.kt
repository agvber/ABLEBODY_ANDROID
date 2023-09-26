package com.smilehunter.ablebody.presentation.item

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.presentation.item.ui.ItemRoute


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