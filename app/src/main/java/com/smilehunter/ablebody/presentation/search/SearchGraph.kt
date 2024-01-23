package com.smilehunter.ablebody.presentation.search

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.search.ui.SearchRoute

fun NavGraphBuilder.addSearchScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onErrorOccur: (ErrorHandlerCode) -> Unit,
    backRequest: () -> Unit,
    productItemClick: (Long) -> Unit,
    codyItemClick: (Long) -> Unit,
) {
    composable(route = "SearchRoute") {
        SearchRoute(
            onErrorOccur = onErrorOccur,
            backRequest = backRequest,
            productItemClick = productItemClick,
            codyItemClick = codyItemClick,
        )
        isBottomBarShow(false)
    }
}