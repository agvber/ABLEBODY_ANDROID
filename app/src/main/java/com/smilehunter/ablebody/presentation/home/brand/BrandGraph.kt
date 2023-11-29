package com.smilehunter.ablebody.presentation.home.brand

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.presentation.home.brand.ui.BrandRoute
import com.smilehunter.ablebody.presentation.main.data.NavigationItems

fun NavGraphBuilder.addBrandScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    onBrandDetailRouteRequest: (Long, String) -> Unit,
) {
    composable(route = NavigationItems.Brand.name) {
        BrandRoute(
            onSearchBarClick = onSearchBarClick,
            onAlertButtonClick = onAlertButtonClick,
            onItemClick = onBrandDetailRouteRequest,
        )
        isBottomBarShow(true)
    }
}