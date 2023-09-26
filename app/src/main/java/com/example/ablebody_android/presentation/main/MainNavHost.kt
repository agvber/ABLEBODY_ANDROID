package com.example.ablebody_android.presentation.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.example.ablebody_android.presentation.bookmark.addBookmarkGraph
import com.example.ablebody_android.presentation.brand.addBrandGraph
import com.example.ablebody_android.presentation.cody_recommended.addCodyRecommendedGraph
import com.example.ablebody_android.presentation.item.addItemGraph
import com.example.ablebody_android.presentation.main.data.NavigationItems
import com.example.ablebody_android.presentation.notification.NotificationRoute
import com.example.ablebody_android.presentation.notification.addNotificationScreen
import com.example.ablebody_android.presentation.search.addSearchScreen

@Composable
fun MainNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = NavigationItems.Brand.name) {

        navigation(startDestination = "BrandListScreen", route = NavigationItems.Brand.name) {
            addBrandGraph(
                onBackClick = { navController.popBackStack() },
                onSearchBarClick = { navController.navigate("SearchRoute") },
                onAlertButtonClick = { navController.navigate(NotificationRoute) },
                brandItemClick = { id, name -> navController.navigate("BrandDetailScreen/$id/$name") },
            )
        }

        navigation(startDestination = "ItemRoute", route = NavigationItems.Item.name) {
            addItemGraph(
                onSearchBarClick = { navController.navigate("SearchRoute") },
                onAlertButtonClick = { navController.navigate(NotificationRoute) },
                productItemClick = { /* TODO productItemDetail 페이지로 가기 */ }
            )
        }

        navigation(startDestination = "CodyRecommendRoute", route = NavigationItems.CodyRecommendation.name) {
            addCodyRecommendedGraph(
                onSearchBarClick = { navController.navigate("SearchRoute") },
                onAlertButtonClick = { navController.navigate(NotificationRoute) },
                codyItemClick = { /* TODO CodyItemDetail 페이지로 가기 */ },
            )
        }

        navigation(startDestination = "BookmarkListRoute", route = NavigationItems.Bookmark.name) {
            addBookmarkGraph(
                onSearchBarClick = { navController.navigate("SearchRoute") },
                onAlertButtonClick = { navController.navigate(NotificationRoute) },
                productItemClick = { /* TODO productItemDetail 페이지로 가기 */ },
                codyItemClick = { /* TODO CodyItemDetail 페이지로 가기 */ },
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