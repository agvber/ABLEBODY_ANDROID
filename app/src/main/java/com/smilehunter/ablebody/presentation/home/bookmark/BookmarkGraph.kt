package com.smilehunter.ablebody.presentation.home.bookmark

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.home.bookmark.ui.BookmarkListRoute
import com.smilehunter.ablebody.presentation.main.data.NavigationItems

fun NavGraphBuilder.addBookmarkScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onErrorRequest: (ErrorHandlerCode) -> Unit,
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    onProductItemDetailRouteRequest: (Long) -> Unit,
    onCodyItemDetailRouteRequest: (Long) -> Unit,
) {
    composable(route = NavigationItems.Bookmark.name) {
        BookmarkListRoute(
            onErrorRequest = onErrorRequest,
            onSearchBarClick = onSearchBarClick,
            onAlertButtonClick = onAlertButtonClick,
            productItemClick = onProductItemDetailRouteRequest,
            codyItemClick = onCodyItemDetailRouteRequest,
        )
        isBottomBarShow(true)
    }
}