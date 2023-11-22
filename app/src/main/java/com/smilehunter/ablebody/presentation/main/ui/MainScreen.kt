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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.smilehunter.ablebody.presentation.main.MainNavHost
import com.smilehunter.ablebody.presentation.main.data.NavigationItems
import com.tosspayments.paymentsdk.PaymentWidget
import kotlinx.coroutines.flow.StateFlow

internal val LocalMainScaffoldPaddingValue = staticCompositionLocalOf {
    PaddingValues()
}

internal val LocalNetworkConnectState = staticCompositionLocalOf {
    true
}

@Composable
fun MainScreen(
    isNetworkConnectionFlow: StateFlow<Boolean>,
    paymentWidget: PaymentWidget
) {
    var isBottomBarShow by rememberSaveable { mutableStateOf(true) }
    var currentNavigationItem by rememberSaveable { mutableStateOf(NavigationItems.Brand) }
    val navController = rememberNavController()
    val isNetworkConnection by isNetworkConnectionFlow.collectAsStateWithLifecycle()

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
            CompositionLocalProvider(
                LocalMainScaffoldPaddingValue.provides(paddingValue),
                LocalNetworkConnectState.provides(isNetworkConnection)
            ) {
                MainNavHost(
                    isBottomBarShow = { isBottomBarShow = it },
                    navController = navController,
                    paymentWidget = paymentWidget
                )
            }
        }
    )
}