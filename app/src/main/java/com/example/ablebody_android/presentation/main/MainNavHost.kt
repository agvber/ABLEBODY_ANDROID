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

@Composable
fun MainNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = NavigationItems.Brand.name) {

        navigation(startDestination = "BrandListScreen", route = NavigationItems.Brand.name) {
            addBrandGraph(
                onBackClick = { navController.popBackStack() },
                brandItemClick = { id, name -> navController.navigate("BrandDetailScreen/$id/$name") },
            )
        }

        navigation(startDestination = "BookmarkListRoute", route = NavigationItems.Bookmark.name) {
            addBookmarkGraph()
        }

        navigation(startDestination = "ItemRoute", route = NavigationItems.Item.name) {
            addItemGraph()
        }

        navigation(startDestination = "CodyRecommendRoute", route = NavigationItems.CodyRecommendation.name) {
            addCodyRecommendedGraph()
        }
    }
}