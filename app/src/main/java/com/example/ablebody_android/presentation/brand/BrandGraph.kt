package com.example.ablebody_android.presentation.brand

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ablebody_android.presentation.brand.ui.BrandDetailRoute
import com.example.ablebody_android.presentation.brand.ui.BrandListRoute

fun NavGraphBuilder.addBrandGraph(
    onBackClick: () -> Unit,
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    brandItemClick: (Long, String) -> Unit,
) {
    composable(route = "BrandListScreen") {
        BrandListRoute(
            onSearchBarClick = onSearchBarClick,
            onAlertButtonClick = onAlertButtonClick,
            onItemClick = brandItemClick,
        )
    }
    composable(route = "BrandDetailScreen/{content_id}/{content_name}",
        arguments = listOf(
            navArgument("content_id") { type = NavType.LongType },
            navArgument("content_name") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        BrandDetailRoute(
            onBackClick = onBackClick,
            contentID = backStackEntry.arguments?.getLong("content_id"),
            contentName = backStackEntry.arguments?.getString("content_name", "")!!
        )
    }
}