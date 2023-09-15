package com.example.ablebody_android.presentation.bookmark

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ablebody_android.presentation.bookmark.ui.BookmarkListRoute

fun NavGraphBuilder.addBookmarkGraph() {
    composable(route = "BookmarkListRoute") {
        BookmarkListRoute()
    }
    composable(route = "BookmarkItemDetailScreen") {

    }
    composable(route = "BookmarkCodyDetailScreen") {

    }
}