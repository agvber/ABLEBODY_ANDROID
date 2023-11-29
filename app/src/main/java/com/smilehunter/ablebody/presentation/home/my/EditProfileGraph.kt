package com.smilehunter.ablebody.presentation.home.my

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.smilehunter.ablebody.presentation.home.my.ui.EditProfileRoute
import com.smilehunter.ablebody.presentation.home.my.ui.SelectProfileImageScreen

const val EditProfileGraph = "edit_profile_graph"

const val EditProfileRoute = "edit_profile_route"

fun NavController.navigateToEditProfileGraph(
    navOptions: NavOptions? = null
) {
    navigate(route = EditProfileGraph, navOptions = navOptions)
}

fun NavGraphBuilder.addEditProfileGraph(
    onBackRequest: () -> Unit,
    defaultImageSelectableViewRequest: () -> Unit,
    nestedGraph: NavGraphBuilder.() -> Unit,
) {

    navigation(
        startDestination = "$EditProfileRoute?" +
                "selected_profile_image_number=null&",
        route = EditProfileGraph,
    ) {

        composable(
            route = "$EditProfileRoute?" +
                    "selected_profile_image_number={selected_profile_image_number}",
            arguments = listOf(
                navArgument("selected_profile_image_number") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val savedStateHandle = navBackStackEntry.savedStateHandle
            val selectedProfileImageNumber by savedStateHandle
                .getStateFlow<Int?>("selected_profile_image_number", null)
                .collectAsStateWithLifecycle()
            EditProfileRoute(
                onBackRequest = onBackRequest,
                defaultImageSelectableViewRequest = defaultImageSelectableViewRequest,
                selectedProfileImageNumber = selectedProfileImageNumber
            )
        }

        nestedGraph()
    }
}

const val SelectProfileImageRoute = "select_profile_image_route"

fun NavController.navigateToSelectProfileImageScreen(
    navOptions: NavOptions? = null
) {
    navigate(route = SelectProfileImageRoute, navOptions = navOptions)
}

fun NavController.selectProfileImageForResult(
    profileImageNumber: Int
) {
    previousBackStackEntry?.savedStateHandle?.set("selected_profile_image_number", profileImageNumber)
    popBackStack()
}


fun NavGraphBuilder.addSelectProfileImageScreen(
    onBackRequest: () -> Unit,
    confirmButtonClick: (Int) -> Unit
) {

    composable(route = SelectProfileImageRoute) {
        SelectProfileImageScreen(
            onBackRequest = onBackRequest,
            confirmButtonClick = confirmButtonClick
        )
    }
}