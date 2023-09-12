package com.example.ablebody_android.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.example.ablebody_android.bookmark.addBookmarkGraph
import com.example.ablebody_android.brand.addBrandGraph

@Composable
fun MainNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "Brand") {

        navigation(startDestination = "BrandListScreen", route = "Brand") {
            addBrandGraph(
                onBackClick = { navController.popBackStack() },
                brandItemClick = { id, name -> navController.navigate("BrandDetailScreen/$id/$name") },
            )
        }

        navigation(startDestination = "BookmarkListRoute", route = "Bookmark") {
            addBookmarkGraph()
        }

    }
}