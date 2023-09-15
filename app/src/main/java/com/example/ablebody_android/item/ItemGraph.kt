package com.example.ablebody_android.item

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.ablebody_android.item.ui.ItemRoute


fun NavGraphBuilder.addItemGraph() {
    composable(route = "ItemRoute") {
        ItemRoute()
    }
}