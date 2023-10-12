package com.smilehunter.ablebody.presentation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.smilehunter.ablebody.presentation.home.bookmark.ui.BookmarkListRoute
import com.smilehunter.ablebody.presentation.home.brand.ui.BrandRoute
import com.smilehunter.ablebody.presentation.home.cody.ui.CodyRecommendedRoute
import com.smilehunter.ablebody.presentation.home.item.ui.ItemRoute
import com.smilehunter.ablebody.presentation.main.data.NavigationItems

const val HomeRoute = "Home"

fun NavGraphBuilder.addHomeGraph(
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    onBrandDetailRouteRequest: (Long, String) -> Unit,
    onProductItemDetailRouteRequest: (Long) -> Unit,
    onCodyItemDetailRouteRequest: (Long) -> Unit,
) {
    navigation(startDestination = NavigationItems.Brand.name, route = "Home") {
        composable(route = NavigationItems.Brand.name) {
            BrandRoute(
                onSearchBarClick = onSearchBarClick,
                onAlertButtonClick = onAlertButtonClick,
                onItemClick = onBrandDetailRouteRequest,
            )
        }
        composable(route = NavigationItems.Item.name) {
            ItemRoute(
                onSearchBarClick = onSearchBarClick,
                onAlertButtonClick = onAlertButtonClick,
                itemClick = onProductItemDetailRouteRequest
            )
        }
        composable(route = NavigationItems.CodyRecommendation.name) {
            CodyRecommendedRoute(
                onSearchBarClick = onSearchBarClick,
                onAlertButtonClick = onAlertButtonClick,
                itemClick = onCodyItemDetailRouteRequest,
            )
        }
        composable(route = NavigationItems.Bookmark.name) {
            BookmarkListRoute(
                onSearchBarClick = onSearchBarClick,
                onAlertButtonClick = onAlertButtonClick,
                productItemClick = onProductItemDetailRouteRequest,
                codyItemClick = onCodyItemDetailRouteRequest,
            )
        }
//        composable(route = NavigationItems.My.name) {
//            MyRoute(
//                onSearchBarClick = onSearchBarClick,
//                onAlertButtonClick = onAlertButtonClick,
//                productItemClick = onProductItemDetailRouteRequest,
//                codyItemClick = onCodyItemDetailRouteRequest,
//            )
//        }
    }
}