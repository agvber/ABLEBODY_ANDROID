package com.smilehunter.ablebody.presentation.brand_detail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.brand_detail.ui.BrandDetailRoute

const val BrandDetailRoute = "brand_detail_route"

fun NavController.navigateToBrandDetailScreen(
    contentID: Long,
    contentName: String,
    navOptions: NavOptions? = null
) {
    navigate(
        route = "${BrandDetailRoute}/$contentID/$contentName",
        navOptions = navOptions
    )
}

fun NavGraphBuilder.addBrandDetailScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onErrorRequest: (ErrorHandlerCode) -> Unit,
    onBackRequest: () -> Unit,
    productItemClick: (Long) -> Unit,
    codyItemClick: (Long) -> Unit,
) {
    composable(route = "${BrandDetailRoute}/{content_id}/{content_name}",
        arguments = listOf(
            navArgument("content_id") { type = NavType.LongType },
            navArgument("content_name") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        BrandDetailRoute(
            onErrorRequest = onErrorRequest,
            onBackClick = onBackRequest,
            productItemClick = productItemClick,
            codyItemClick = codyItemClick,
        )
        isBottomBarShow(true)
    }
}