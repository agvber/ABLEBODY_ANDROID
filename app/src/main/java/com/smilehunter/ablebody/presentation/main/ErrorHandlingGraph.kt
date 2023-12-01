package com.smilehunter.ablebody.presentation.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.presentation.main.ui.error_handling.InternalServerError
import com.smilehunter.ablebody.presentation.main.ui.error_handling.NotFoundErrorScreen

internal fun NavController.navigateErrorHandlingScreen(
    errorHandlerCode: ErrorHandlerCode
) {
    when (errorHandlerCode) {
        ErrorHandlerCode.NOT_FOUND_ERROR -> navigateToNotFoundErrorScreen()
        ErrorHandlerCode.INTERNAL_SERVER_ERROR -> navigateToInternalServerErrorScreen()
    }
}


const val NotFoundErrorRoute = "not_found_error_route"

fun NavController.navigateToNotFoundErrorScreen(
    navOptions: NavOptions? = null
) {
    navigate(route = NotFoundErrorRoute, navOptions = navOptions)
}

fun NavGraphBuilder.addNotFoundErrorScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    composable(route = NotFoundErrorRoute) {
        NotFoundErrorScreen(onClick = onClick)
        isBottomBarShow(false)
    }
}

const val InternalServerErrorRoute = "internal_server_error_route"

fun NavController.navigateToInternalServerErrorScreen(
    navOptions: NavOptions? = null
) {
    navigate(route = InternalServerErrorRoute, navOptions = navOptions)
}

fun NavGraphBuilder.addInternalServerErrorScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    composable(route = InternalServerErrorRoute) {
        InternalServerError(onClick = onClick)
        isBottomBarShow(false)
    }
}