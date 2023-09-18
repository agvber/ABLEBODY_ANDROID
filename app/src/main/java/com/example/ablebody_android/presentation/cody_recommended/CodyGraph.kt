package com.example.ablebody_android.presentation.cody_recommended

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ablebody_android.presentation.cody_recommended.ui.CodyRecommendedRoute

fun NavGraphBuilder.addCodyRecommendedGraph() {
    composable(route = "CodyRecommendRoute") {
        CodyRecommendedRoute()
    }
}