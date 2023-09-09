package com.example.ablebody_android.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.brand.addBrandGraph

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Brand") {

        navigation(startDestination = "BrandListScreen", route = "Brand") {
            addBrandGraph(
                onBackClick = { navController.popBackStack() },
                brandItemClick = { id, name -> navController.navigate("BrandDetailScreen/$id/$name") },
            )
        }

    }
}