package com.smilehunter.ablebody.presentation.main.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.smilehunter.ablebody.presentation.main.MainNavHost
import com.smilehunter.ablebody.presentation.main.data.NavigationItems

val scaffoldPaddingValueCompositionLocal = staticCompositionLocalOf {
    PaddingValues()
}

@Composable
fun MainScreen() {
    var isBottomBarShow by remember { mutableStateOf(true) }
    var currentNavigationItem by remember { mutableStateOf(NavigationItems.Brand) }
    val navController = rememberNavController()
    val uriHandler = LocalUriHandler.current

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarShow,
                enter = slideInVertically(animationSpec = tween(700), initialOffsetY = { it }),
                exit = slideOutVertically(animationSpec = tween(700), targetOffsetY = { it })
            ) {
                MainNavigationBar(
                    selected = currentNavigationItem,
                    onChangeValue = {
                        navController.navigate(it.name) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.startDestinationId) {
                                if (currentNavigationItem == it) {
                                    inclusive = true
                                } else {
                                    saveState = true
                                }
                            }
                        }
                        currentNavigationItem = it
                    }
                )
            }
        },
        content = { paddingValue ->
            CompositionLocalProvider(scaffoldPaddingValueCompositionLocal.provides(paddingValue)) {
                MainNavHost(
                    isBottomBarShow = { isBottomBarShow = it },
                    uriRequest = { uriHandler.openUri(it) },
                    navController = navController
                )
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}