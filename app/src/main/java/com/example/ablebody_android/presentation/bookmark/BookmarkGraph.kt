package com.example.ablebody_android.presentation.bookmark

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ablebody_android.presentation.bookmark.ui.BookmarkListRoute

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