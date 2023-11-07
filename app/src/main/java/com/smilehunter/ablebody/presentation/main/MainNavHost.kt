package com.smilehunter.ablebody.presentation.main

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.smilehunter.ablebody.presentation.brand_detail.addBrandDetailScreen
import com.smilehunter.ablebody.presentation.brand_detail.navigateToBrandDetailScreen
import com.smilehunter.ablebody.presentation.creator_detail.addCreatorDetailScreen
import com.smilehunter.ablebody.presentation.creator_detail.navigateToCreatorDetail
import com.smilehunter.ablebody.presentation.home.HomeRoute
import com.smilehunter.ablebody.presentation.home.addHomeGraph
import com.smilehunter.ablebody.presentation.like_list.addLikeUserListScreen
import com.smilehunter.ablebody.presentation.like_list.navigateToLikeUserListScreen
import com.smilehunter.ablebody.presentation.item_detail.ui.ItemDetailScreen
import com.smilehunter.ablebody.presentation.item_detail.ui.ItemReviewScreen
import com.smilehunter.ablebody.presentation.notification.NotificationRoute
import com.smilehunter.ablebody.presentation.notification.addNotificationScreen
import com.smilehunter.ablebody.presentation.search.addSearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavHost(
    isBottomBarShow: (Boolean) -> Unit,
    uriRequest: (String) -> Unit,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        addHomeGraph(
            isBottomBarShow = isBottomBarShow,
            onSearchBarClick = { navController.navigate("SearchRoute") },
            onAlertButtonClick = { navController.navigate(NotificationRoute) },
            onBrandDetailRouteRequest = navController::navigateToBrandDetailScreen,
            onProductItemDetailRouteRequest = { navController.navigate("ItemDetailScreen/$it")},
            onCodyItemDetailRouteRequest = navController::navigateToCreatorDetail,
        )

        addSearchScreen(
            isBottomBarShow = isBottomBarShow,
            backRequest = navController::popBackStack,
            productItemClick = { /* TODO ProductItemDetail 페이지로 가기 */ },
            codyItemClick = navController::navigateToCreatorDetail,
        )

        addNotificationScreen(
            isBottomBarShow = isBottomBarShow,
            onBackRequest = navController::popBackStack,
            itemClick = { /* TODO 클릭시 해당 게시글로 이동 */ }
        )

        addBrandDetailScreen(
            onBackRequest = navController::popBackStack,
            isBottomBarShow = isBottomBarShow,
            productItemClick = { /* TODO productItemDetail 페이지로 가기 */ },
            codyItemClick = navController::navigateToCreatorDetail,
        )

        addCreatorDetailScreen(
            isBottomBarShow = isBottomBarShow,
            onBackRequest = navController::popBackStack,
            profileRequest = { /* TODO 다른 유저의 Profile 화면으로 가기 */ },
            commentButtonOnClick = { /* TODO 댓글 페이지 가기 */ },
            likeCountButtonOnClick = navController::navigateToLikeUserListScreen,
            snsShortcutButtonOnClick = { uriRequest(it) },
            productItemOnClick = { /* TODO ProductItemDetail 페이지로 가기 */ },
        )

        addLikeUserListScreen(
            isBottomBarShow = isBottomBarShow,
            onBackRequest = navController::popBackStack,
            profileRequest = { /* TODO 다른 유저의 Profile 화면으로 가기 */ },
        )

//        addCommentScreen(
//            onBackRequest = navController::popBackStack,
//            isBottomBarShow = isBottomBarShow
//        )



        composable(route = "ItemDetailScreen/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType}
            )
        ){ navBackStackEntry ->
            navBackStackEntry.arguments?.getLong("id")?.let {
                ItemDetailScreen(
                    id = it,
                    itemClick = { item_id,review_id ->
                        Log.d("itemClick", "$item_id $review_id")
                        navController.navigate("ItemReviewScreen/$item_id/$review_id")
                    },
                    onBackRequest = navController::popBackStack,
                    purchaseOnClick = { },
                    brandOnClick = { item_id, item_name ->
                        navController.navigateToBrandDetailScreen(contentID = item_id, contentName = item_name) }
                )
            }
            isBottomBarShow(false)
        }

        composable(route = "ItemReviewScreen/{item_id}/{review_id}",
            arguments = listOf(
                navArgument("item_id") { type = NavType.LongType},
                navArgument("review_id") { type = NavType.LongType}
            )
        ){ navBackStackEntry ->
            val itemId = navBackStackEntry.arguments?.getLong("item_id")
            val reviewId = navBackStackEntry.arguments?.getLong("review_id")
            ItemReviewScreen(
                id = itemId!!,
                reviewId = reviewId!!,
                onBackRequest = navController::popBackStack
            )
        }
    }
}