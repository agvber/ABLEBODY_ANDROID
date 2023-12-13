package com.smilehunter.ablebody.presentation.creator_detail

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.smilehunter.ablebody.model.ErrorHandlerCode
import com.smilehunter.ablebody.model.LikedLocations
import com.smilehunter.ablebody.presentation.creator_detail.ui.CreatorDetailRoute

internal const val CreatorDetailRoute = "creator_detail_route"

fun NavController.navigateToCreatorDetail(
    itemID: Long,
    navOptions: NavOptions? = null,
) {
    navigate(route = "$CreatorDetailRoute/$itemID", navOptions = navOptions)
}

fun NavGraphBuilder.addCreatorDetailScreen(
    isBottomBarShow: (Boolean) -> Unit,
    onErrorRequest: (ErrorHandlerCode) -> Unit,
    onBackRequest: () -> Unit,
    profileRequest: (String) -> Unit,
    commentButtonOnClick: (Long) -> Unit,
    likeCountButtonOnClick: (Long, LikedLocations) -> Unit,
    productItemOnClick: (Long) -> Unit,
) {
    composable(
        route = "$CreatorDetailRoute/{content_id}",
        arguments = listOf(
            navArgument("content_id") { type = NavType.LongType }
        ),
        deepLinks = listOf(
            NavDeepLink("ablebody-app://ablebody.im/home/creator/{content_id}")
        ),
        exitTransition = { fadeOut(tween(100)) }
    ) { backStackEntry ->
        CreatorDetailRoute(
            onBackRequest = onBackRequest,
            onErrorRequest = onErrorRequest,
            profileRequest = profileRequest,
            commentButtonOnClick = commentButtonOnClick,
            likeCountButtonOnClick = { likeCountButtonOnClick(it, LikedLocations.BOARD) },
            productItemOnClick = productItemOnClick
        )
        isBottomBarShow(false)
    }
}
