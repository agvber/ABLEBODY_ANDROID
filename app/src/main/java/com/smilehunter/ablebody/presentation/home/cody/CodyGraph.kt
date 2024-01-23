package com.smilehunter.ablebody.presentation.home.cody

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.home.cody.ui.CodyRecommendedRoute
import com.smilehunter.ablebody.presentation.main.data.NavigationItems

fun NavGraphBuilder.addCodyScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onErrorRequest: (ErrorHandlerCode) -> Unit,
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    onCodyItemDetailRouteRequest: (Long) -> Unit,
) {
    composable(
        route = NavigationItems.CodyRecommendation.name,
    ) {
        CodyRecommendedRoute(
            onErrorRequest = onErrorRequest,
            onSearchBarClick = onSearchBarClick,
            onAlertButtonClick = onAlertButtonClick,
            itemClick = onCodyItemDetailRouteRequest,
        )
        isBottomBarShow(true)
    }
}