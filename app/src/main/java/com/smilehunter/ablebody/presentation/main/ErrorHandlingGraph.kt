package com.smilehunter.ablebody.presentation.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.presentation.main.ui.error_handling.InternalServerError
import com.smilehunter.ablebody.presentation.main.ui.error_handling.NotFoundErrorScreen

const val NotFoundErrorRoute = "not_found_error_route"

fun NavController.navigateToNotFoundErrorScreen(
    navOptions: NavOptions? = null
) {
    navigate(route = NotFoundErrorRoute, navOptions = navOptions)
}

fun NavGraphBuilder.addNotFoundErrorScreen(
    onClick: () -> Unit
) {
    composable(route = NotFoundErrorRoute) {
        NotFoundErrorScreen(onClick = onClick)
    }
}

const val InternalServerErrorRoute = "internal_server_error_route"

fun NavController.navigateToInternalServerErrorScreen(
    navOptions: NavOptions? = null
) {
    navigate(route = InternalServerErrorRoute, navOptions = navOptions)
}

fun NavGraphBuilder.addInternalServerErrorScreen(
    onClick: () -> Unit
) {
    composable(route = InternalServerErrorRoute) {
        InternalServerError(onClick = onClick)
    }
}