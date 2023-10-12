package com.smilehunter.ablebody.presentation.like_list

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.smilehunter.ablebody.presentation.like_list.ui.LikeListRoute


const val LikeUserListRoute = "like_user_list_route"

fun NavController.navigateToLikeUserListScreen(
    contentID: Long,
    navOptions: NavOptions? = null,
) {
    navigate(route = "$LikeUserListRoute/$contentID", navOptions = navOptions)
}


fun NavGraphBuilder.addLikeUserListScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onBackRequest: () -> Unit,
    profileRequest: (String) -> Unit,
) {
    
    composable(
        route = "$LikeUserListRoute/{content_id}",
        arguments = listOf(
            navArgument("content_id") { type = NavType.LongType }
        )
    ) {  backStackEntry ->
        LikeListRoute(
            onBackRequest = onBackRequest,
            profileRequest = profileRequest,
            contentID = backStackEntry.arguments?.getLong("content_id", 0)!!
        )
        isBottomBarShow(false)
    }
}