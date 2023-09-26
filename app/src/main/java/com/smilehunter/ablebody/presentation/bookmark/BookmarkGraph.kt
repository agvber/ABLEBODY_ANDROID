package com.smilehunter.ablebody.presentation.bookmark

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.presentation.bookmark.ui.BookmarkListRoute

fun NavGraphBuilder.addBookmarkGraph(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    productItemClick: (Long) -> Unit,
    codyItemClick: (Long) -> Unit,
) {
    composable(route = "BookmarkListRoute") {
        BookmarkListRoute(
            onSearchBarClick = onSearchBarClick,
            onAlertButtonClick = onAlertButtonClick,
            productItemClick = productItemClick,
            codyItemClick = codyItemClick
        )
    }
}