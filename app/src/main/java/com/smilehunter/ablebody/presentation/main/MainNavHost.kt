package com.smilehunter.ablebody.presentation.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.smilehunter.ablebody.presentation.brand_detail.ui.BrandDetailRoute
import com.smilehunter.ablebody.presentation.home.HomeRoute
import com.smilehunter.ablebody.presentation.home.addHomeGraph
import com.smilehunter.ablebody.presentation.notification.NotificationRoute
import com.smilehunter.ablebody.presentation.notification.addNotificationScreen
import com.smilehunter.ablebody.presentation.search.addSearchScreen

@Composable
fun MainNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        addHomeGraph(
            onSearchBarClick = { navController.navigate("SearchRoute") },
            onAlertButtonClick = { navController.navigate(NotificationRoute) },
            onBrandDetailRouteRequest = { id, name -> navController.navigate("BrandDetailScreen/$id/$name") },
            onProductItemDetailRouteRequest = { /* TODO productItemDetail 페이지로 가기 */ },
            onCodyItemDetailRouteRequest = { /* TODO CodyItemDetail 페이지로 가기 */ },
        )

        composable(route = "BrandDetailScreen/{content_id}/{content_name}",
            arguments = listOf(
                navArgument("content_id") { type = NavType.LongType },
                navArgument("content_name") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            BrandDetailRoute(
                onBackClick = { navController.popBackStack() },
                contentID = backStackEntry.arguments?.getLong("content_id"),
                contentName = backStackEntry.arguments?.getString("content_name", "")!!
            )
        }

        addSearchScreen(
            backRequest = { navController.popBackStack() },
            productItemClick = { /* TODO ProductItemDetail 페이지로 가기 */ },
            codyItemClick = { /* TODO CodyItemDetail 페이지로 가기 */ },
        )

        addNotificationScreen(
            onBackRequest = { navController.popBackStack() },
            itemClick = { /* TODO 클릭시 해당 게시글로 이동 */ }
        )
    }
}