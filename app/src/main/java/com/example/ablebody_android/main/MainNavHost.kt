package com.example.ablebody_android.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.example.ablebody_android.bookmark.addBookmarkGraph
import com.example.ablebody_android.brand.addBrandGraph
import com.example.ablebody_android.item.addItemGraph
import com.example.ablebody_android.main.data.NavigationItems

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
    }
}