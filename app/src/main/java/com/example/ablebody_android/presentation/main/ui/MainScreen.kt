package com.example.ablebody_android.presentation.main.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.ablebody_android.presentation.main.MainNavHost
import com.example.ablebody_android.presentation.main.data.NavigationItems

val scaffoldPaddingValueCompositionLocal = staticCompositionLocalOf {
    PaddingValues()
}

@Composable
fun MainScreen() {
    var isBottomBarShow by remember { mutableStateOf(true) }
    var currentNavigationItem by remember { mutableStateOf(NavigationItems.Brand) }
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            if (isBottomBarShow) {
                MainNavigationBar(
                    selected = currentNavigationItem,
                    onChangeValue = {
                        currentNavigationItem = it
                        navController.navigate(it.name) {
                            popUpTo(navController.graph.id){
                                inclusive = true
                            }
                        }
                    }
                )
            }
        },
        content = { paddingValue ->
            CompositionLocalProvider(scaffoldPaddingValueCompositionLocal.provides(paddingValue)) {
                MainNavHost(navController = navController)
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}