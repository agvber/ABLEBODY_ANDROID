package com.smilehunter.ablebody.presentation.main

import android.net.Uri
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
import com.smilehunter.ablebody.presentation.comment.addCommentScreen
import com.smilehunter.ablebody.presentation.comment.navigateToCommentScreen
import com.smilehunter.ablebody.presentation.creator_detail.addCreatorDetailScreen
import com.smilehunter.ablebody.presentation.creator_detail.navigateToCreatorDetail
import com.smilehunter.ablebody.presentation.delivery.deliveryScreen
import com.smilehunter.ablebody.presentation.delivery.navigateToDeliveryScreen
import com.smilehunter.ablebody.presentation.delivery.navigateToSearchPostCodeWebViewScreen
import com.smilehunter.ablebody.presentation.delivery.popBackStackForResult
import com.smilehunter.ablebody.presentation.delivery.searchPostCodeWebViewScreen
import com.smilehunter.ablebody.presentation.home.HomeRoute
import com.smilehunter.ablebody.presentation.home.addHomeGraph
import com.smilehunter.ablebody.presentation.home.bookmark.addBookmarkScreen
import com.smilehunter.ablebody.presentation.home.brand.addBrandScreen
import com.smilehunter.ablebody.presentation.home.cody.addCodyScreen
import com.smilehunter.ablebody.presentation.home.item.addItemScreen
import com.smilehunter.ablebody.presentation.home.my.addEditProfileGraph
import com.smilehunter.ablebody.presentation.home.my.addSelectProfileImageScreen
import com.smilehunter.ablebody.presentation.home.my.navigateToSelectProfileImageScreen
import com.smilehunter.ablebody.presentation.home.my.selectProfileImageForResult
import com.smilehunter.ablebody.presentation.item_detail.ui.ItemDetailScreen
import com.smilehunter.ablebody.presentation.item_detail.ui.ItemReviewScreen
import com.smilehunter.ablebody.presentation.like_list.addLikeUserListScreen
import com.smilehunter.ablebody.presentation.like_list.navigateToLikeUserListScreen
import com.smilehunter.ablebody.presentation.my.myInfo.ui.WithdrawScreenRoute
import com.smilehunter.ablebody.presentation.notification.NotificationRoute
import com.smilehunter.ablebody.presentation.notification.addNotificationScreen
import com.smilehunter.ablebody.presentation.order_management.addOrderItemDetailScreen
import com.smilehunter.ablebody.presentation.order_management.addOrderManagementGraph
import com.smilehunter.ablebody.presentation.order_management.navigateToOrderItemDetailScreen
import com.smilehunter.ablebody.presentation.payment.addPaymentGraph
import com.smilehunter.ablebody.presentation.receipt.addReceiptScreen
import com.smilehunter.ablebody.presentation.receipt.navigateToReceiptScreen
import com.smilehunter.ablebody.presentation.search.addSearchScreen
import com.tosspayments.paymentsdk.PaymentWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavHost(
    isBottomBarShow: (Boolean) -> Unit,
    navController: NavHostController,
    paymentWidget: PaymentWidget
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        addHomeGraph(
            isBottomBarShow = isBottomBarShow,
            settingOnClickRouteRequest = {navController.navigate("SettingScreen")},
            onBackRequest = navController::popBackStack,
            suggestonClick = {navController.navigate("SuggestScreen")},
            myInfoOnClick = {navController.navigate("MyInfoScreenRoute")},
            alarmOnClick = {navController.navigate("AlarmScreen")},
            withDrawOnClick = {navController.navigate("WithdrawBeforeScreen")},
            editButtonOnClick = {navController.navigate("MyInfomationEditScreen")},
            withDrawReasonOnClick = {navController.navigate("WithdrawScreenRoute/$it")},
//            withDrawReasonOnClick = {Log.d("MainNavHost 탈퇴 이유", it)},
            coupononClick = {navController.navigate("CouponRoute")},
            couponRegisterOnClick = {navController.navigate("CouponRegisterRoute")},
            onReport = {navController.navigate("ReportRoute")},
            withDrawButtonOnClick = {navController.navigate("")},
            orderManagementOnClick = {navController.navigate("")},
            nestedGraph = {
                addBrandScreen(
                    isBottomBarShow = isBottomBarShow,
                    onSearchBarClick = { navController.navigate("SearchRoute") },
                    onAlertButtonClick = { navController.navigate(NotificationRoute) },
                    onBrandDetailRouteRequest = navController::navigateToBrandDetailScreen
                )
                addItemScreen(
                    isBottomBarShow = isBottomBarShow,
                    onSearchBarClick = { navController.navigate("SearchRoute") },
                    onAlertButtonClick = { navController.navigate(NotificationRoute) },
                    onProductItemDetailRouteRequest = { navController.navigate("ItemDetailScreen/$it")},
                )
                addCodyScreen(
                    isBottomBarShow = isBottomBarShow,
                    onSearchBarClick = { navController.navigate("SearchRoute") },
                    onAlertButtonClick = { navController.navigate(NotificationRoute) },
                    onCodyItemDetailRouteRequest = navController::navigateToCreatorDetail,
                )
                addBookmarkScreen(
                    isBottomBarShow = isBottomBarShow,
                    onSearchBarClick = { navController.navigate("SearchRoute") },
                    onAlertButtonClick = { navController.navigate(NotificationRoute) },
                    onProductItemDetailRouteRequest = { navController.navigate("ItemDetailScreen/$it")},
                    onCodyItemDetailRouteRequest = navController::navigateToCreatorDetail,
                )
                addEditProfileGraph(
                    onBackRequest = navController::popBackStack,
                    defaultImageSelectableViewRequest = navController::navigateToSelectProfileImageScreen,
                    nestedGraph = {
                        addSelectProfileImageScreen(
                            onBackRequest = navController::popBackStack,
                            confirmButtonClick = navController::selectProfileImageForResult
                        )
                    }
                )
            }
        )

        addSearchScreen(
            isBottomBarShow = isBottomBarShow,
            backRequest = navController::popBackStack,
            productItemClick = { navController.navigate("ItemDetailScreen/$it") },
            codyItemClick = navController::navigateToCreatorDetail,
        )

        addNotificationScreen(
            isBottomBarShow = isBottomBarShow,
            onBackRequest = navController::popBackStack,
            itemClick = { uri -> navController.navigate(deepLink = Uri.parse(uri)) }
        )

        addBrandDetailScreen(
            onBackRequest = navController::popBackStack,
            isBottomBarShow = isBottomBarShow,
            productItemClick = { navController.navigate("ItemDetailScreen/$it") },
            codyItemClick = navController::navigateToCreatorDetail,
        )

        addCreatorDetailScreen(
            isBottomBarShow = isBottomBarShow,
            onBackRequest = navController::popBackStack,
            profileRequest = { navController.navigate("OtherNormalUserRoute/$it")
                Log.d("다른 유저 프로필", it)},
            commentButtonOnClick = navController::navigateToCommentScreen,
            likeCountButtonOnClick = navController::navigateToLikeUserListScreen,
            productItemOnClick = { navController.navigate("ItemDetailScreen/$it") },
        )

        addLikeUserListScreen(
            isBottomBarShow = isBottomBarShow,
            onBackRequest = navController::popBackStack,
            profileRequest = { navController.navigate("OtherNormalUserRoute/$it")
                Log.d("다른 유저 프로필", it)},
        )

        addCommentScreen(
            onBackRequest = navController::popBackStack,
            onUserProfileVisitRequest = { /* TODO 다른 유저의 Profile 화면으로 가기 */ },
            likeUsersViewOnRequest = navController::navigateToLikeUserListScreen,
            isBottomBarShow = isBottomBarShow
        )

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
                        navController.navigateToBrandDetailScreen(contentID = item_id, contentName = item_name)
                    },
                    codyOnClick = {
                        Log.d("코디 클릭됨",it.toString())
                        navController::navigateToCreatorDetail
                    }
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

        //탈퇴 화면
        composable(route = "WithdrawScreenRoute/{draw_reason}",
            arguments = listOf(
                navArgument("draw_reason") { type = NavType.StringType}
            )
        ){ navBackStackEntry ->
            val draw_reason = navBackStackEntry.arguments?.getString("draw_reason")

            WithdrawScreenRoute(
                onBackRequest = navController::popBackStack,
                drawReason = draw_reason!!,
                withDrawButtonOnClick = {Log.d("탈퇴하기", "탈퇴하기 버튼 눌려짐")}//{navController.navigate("WithDrawCompleteScreen")}
            )
            isBottomBarShow(false)
        }

        addPaymentGraph(
            onBackRequest = navController::popBackStack,
            addressRequest = navController::navigateToDeliveryScreen,
            receiptRequest = navController::navigateToReceiptScreen,
            nestedGraphs = {
                deliveryScreen(
                    onBackRequest = navController::popBackStack,
                    postCodeRequest = navController::navigateToSearchPostCodeWebViewScreen,
                    isBottomBarShow = isBottomBarShow
                )
                searchPostCodeWebViewScreen(
                    onFinished = navController::popBackStackForResult,
                    isBottomBarShow = isBottomBarShow
                )
                addReceiptScreen(orderComplete = { /* TODO 브랜드 홈으로 가기 */ })
            },
            isBottomBarShow = isBottomBarShow,
            paymentWidget = paymentWidget
        )

        addOrderManagementGraph(
            onBackRequest = navController::popBackStack,
            itemOnClick = navController::navigateToOrderItemDetailScreen,
            nestedGraphs = {
                addOrderItemDetailScreen(
                    onBackRequest = navController::popBackStack,
                    isBottomBarShow = isBottomBarShow
                )
            },
            isBottomBarShow = isBottomBarShow
        )
    }
}