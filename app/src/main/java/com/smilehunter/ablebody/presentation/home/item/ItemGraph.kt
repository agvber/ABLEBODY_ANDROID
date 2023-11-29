package com.smilehunter.ablebody.presentation.home.item

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.presentation.home.item.ui.ItemRoute
import com.smilehunter.ablebody.presentation.main.data.NavigationItems

fun NavGraphBuilder.addItemScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    onProductItemDetailRouteRequest: (Long) -> Unit,
) {
    composable(route = NavigationItems.Item.name) {
        ItemRoute(
            onSearchBarClick = onSearchBarClick,
            onAlertButtonClick = onAlertButtonClick,
            itemClick = onProductItemDetailRouteRequest
        )
        isBottomBarShow(true)
    }
}