package com.smilehunter.ablebody.presentation.like_list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.model.LikedLocations
import com.smilehunter.ablebody.presentation.like_list.ui.LikeListRoute


const val LikeUserListRoute = "like_user_list_route"

fun NavController.navigateToLikeUserListScreen(
    contentID: Long,
    likedLocations: LikedLocations,
    navOptions: NavOptions? = null,
) {
    navigate(route = "$LikeUserListRoute/$contentID/${likedLocations.name}", navOptions = navOptions)
}


fun NavGraphBuilder.addLikeUserListScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onErrorRequest: (ErrorHandlerCode) -> Unit,
    onBackRequest: () -> Unit,
    profileRequest: (String) -> Unit,
) {
    
    composable(
        route = "$LikeUserListRoute/{content_id}/{like_location}",
        arguments = listOf(
            navArgument("content_id") { type = NavType.LongType },
            navArgument("like_location") { type = NavType.StringType }
        )
    ) {  backStackEntry ->
        LikeListRoute(
            onErrorRequest = onErrorRequest,
            onBackRequest = onBackRequest,
            profileRequest = profileRequest
        )
        isBottomBarShow(false)
    }
}