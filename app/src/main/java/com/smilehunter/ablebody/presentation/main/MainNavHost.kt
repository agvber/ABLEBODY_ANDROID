package com.smilehunter.ablebody.presentation.main

import android.net.Uri
import android.util.Log
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
import com.smilehunter.ablebody.presentation.item_detail.addItemDetailGraph
import com.smilehunter.ablebody.presentation.item_detail.navigateToItemDetailGraph
import com.smilehunter.ablebody.presentation.item_review.addItemReviewScreen
import com.smilehunter.ablebody.presentation.item_review.navigateToItemReview
import com.smilehunter.ablebody.presentation.like_list.addLikeUserListScreen
import com.smilehunter.ablebody.presentation.like_list.navigateToLikeUserListScreen
import com.smilehunter.ablebody.presentation.my.myInfo.ui.WithdrawScreenRoute
import com.smilehunter.ablebody.presentation.notification.NotificationRoute
import com.smilehunter.ablebody.presentation.notification.addNotificationScreen
import com.smilehunter.ablebody.presentation.order_management.addOrderItemDetailScreen
import com.smilehunter.ablebody.presentation.order_management.addOrderManagementGraph
import com.smilehunter.ablebody.presentation.order_management.navigateToOrderItemDetailScreen
import com.smilehunter.ablebody.presentation.payment.addPaymentGraph
import com.smilehunter.ablebody.presentation.payment.navigateToPayment
import com.smilehunter.ablebody.presentation.receipt.addReceiptScreen
import com.smilehunter.ablebody.presentation.receipt.navigateToReceiptScreen
import com.smilehunter.ablebody.presentation.search.addSearchScreen
import com.tosspayments.paymentsdk.PaymentWidget

@Composable
fun MainNavHost(
    recreateRequest: () -> Unit,
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
            nestedGraph = {
                addBrandScreen(
                    isBottomBarShow = isBottomBarShow,
                    onErrorRequest = navController::navigateErrorHandlingScreen,
                    onSearchBarClick = { navController.navigate("SearchRoute") },
                    onAlertButtonClick = { navController.navigate(NotificationRoute) },
                    onBrandDetailRouteRequest = navController::navigateToBrandDetailScreen
                )
                addItemScreen(
                    isBottomBarShow = isBottomBarShow,
                    onErrorRequest = navController::navigateErrorHandlingScreen,
                    onSearchBarClick = { navController.navigate("SearchRoute") },
                    onAlertButtonClick = { navController.navigate(NotificationRoute) },
                    onProductItemDetailRouteRequest = navController::navigateToItemDetailGraph,
                )
                addCodyScreen(
                    isBottomBarShow = isBottomBarShow,
                    onErrorRequest = navController::navigateErrorHandlingScreen,
                    onSearchBarClick = { navController.navigate("SearchRoute") },
                    onAlertButtonClick = { navController.navigate(NotificationRoute) },
                    onCodyItemDetailRouteRequest = navController::navigateToCreatorDetail,
                )
                addBookmarkScreen(
                    isBottomBarShow = isBottomBarShow,
                    onErrorRequest = navController::navigateErrorHandlingScreen,
                    onSearchBarClick = { navController.navigate("SearchRoute") },
                    onAlertButtonClick = { navController.navigate(NotificationRoute) },
                    onProductItemDetailRouteRequest = navController::navigateToItemDetailGraph,
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
            onErrorOccur = navController::navigateErrorHandlingScreen,
            backRequest = navController::popBackStack,
            productItemClick = navController::navigateToItemDetailGraph,
            codyItemClick = navController::navigateToCreatorDetail,
        )

        addNotificationScreen(
            isBottomBarShow = isBottomBarShow,
            onErrorRequest = navController::navigateErrorHandlingScreen,
            onBackRequest = navController::popBackStack,
            itemClick = { uri -> navController.navigate(deepLink = Uri.parse(uri)) }
        )

        addBrandDetailScreen(
            isBottomBarShow = isBottomBarShow,
            onErrorRequest = navController::navigateErrorHandlingScreen,
            onBackRequest = navController::popBackStack,
            productItemClick = navController::navigateToItemDetailGraph,
            codyItemClick = navController::navigateToCreatorDetail,
        )

        addCreatorDetailScreen(
            isBottomBarShow = isBottomBarShow,
            onErrorRequest = navController::navigateErrorHandlingScreen,
            onBackRequest = navController::popBackStack,
            profileRequest = { navController.navigate("OtherNormalUserRoute/$it")
                Log.d("다른 유저 프로필", it)},
            commentButtonOnClick = navController::navigateToCommentScreen,
            likeCountButtonOnClick = navController::navigateToLikeUserListScreen,
            productItemOnClick = navController::navigateToItemDetailGraph
        )

        addLikeUserListScreen(
            isBottomBarShow = isBottomBarShow,
            onErrorRequest = navController::navigateErrorHandlingScreen,
            onBackRequest = navController::popBackStack,
            profileRequest = { navController.navigate("OtherNormalUserRoute/$it")
                Log.d("다른 유저 프로필", it)},
        )

        addCommentScreen(
            onErrorRequest = navController::navigateErrorHandlingScreen,
            onBackRequest = navController::popBackStack,
            onUserProfileVisitRequest = { /* TODO 다른 유저의 Profile 화면으로 가기 */ },
            likeUsersViewOnRequest = navController::navigateToLikeUserListScreen,
            isBottomBarShow = isBottomBarShow
        )

        addItemDetailGraph(
            isBottomBarShow = isBottomBarShow,
            onBackRequest = navController::popBackStack,
            brandOnClick = navController::navigateToBrandDetailScreen,
            codyOnClick = navController::navigateToCreatorDetail,
            itemReview = navController::navigateToItemReview,
            purchaseOnClick = navController::navigateToPayment
        ) {
            addItemReviewScreen(
                isBottomShow = isBottomBarShow,
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
            isBottomBarShow = isBottomBarShow,
            onErrorOccur = navController::navigateErrorHandlingScreen,
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
                addReceiptScreen(
                    onErrorOccur = navController::navigateErrorHandlingScreen,
                    orderComplete = recreateRequest
                )
            },
            paymentWidget = paymentWidget
        )

        addOrderManagementGraph(
            isBottomBarShow = isBottomBarShow,
            onErrorOccur = navController::navigateErrorHandlingScreen,
            onBackRequest = navController::popBackStack,
            itemOnClick = navController::navigateToOrderItemDetailScreen,
            nestedGraphs = {
                addOrderItemDetailScreen(
                    isBottomBarShow = isBottomBarShow,
                    onErrorRequest = navController::navigateErrorHandlingScreen,
                    onBackRequest = navController::popBackStack,
                )
            }
        )

        addNotFoundErrorScreen(
            isBottomBarShow = isBottomBarShow,
            onClick = {
                navController.popBackStack()
                navController.popBackStack()
            }
        )

        addInternalServerErrorScreen(
            isBottomBarShow = isBottomBarShow,
            onClick = recreateRequest
        )
    }
}