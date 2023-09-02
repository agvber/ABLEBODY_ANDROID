package com.example.ablebody_android.brand.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun BrandNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "BrandListScreen") {
        brandListScreen(onItemClick = { id, name -> navController.navigate("BrandDetailScreen/$id/$name") })
        brandDetailScreen(onBackClick = { navController.popBackStack() })
    }
}

private fun NavGraphBuilder.brandListScreen(
    onItemClick: (Long, String) -> Unit
) {
    composable(route = "BrandListScreen") {
        BrandListRoute(onItemClick = onItemClick)
    }
}
private fun NavGraphBuilder.brandDetailScreen(
    onBackClick: () -> Unit
) {
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