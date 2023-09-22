package com.example.ablebody_android.presentation.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ablebody_android.presentation.search.ui.SearchRoute

fun NavGraphBuilder.addSearchScreen(
    backRequest: () -> Unit,
    productItemClick: (Long) -> Unit,
    codyItemClick: (Long) -> Unit,
) {
    composable(route = "SearchRoute") {
        SearchRoute(
            backRequest = backRequest,
            productItemClick = productItemClick,
            codyItemClick = codyItemClick,
        )
    }
}