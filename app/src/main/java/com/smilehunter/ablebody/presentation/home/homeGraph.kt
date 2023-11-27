package com.smilehunter.ablebody.presentation.home

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.smilehunter.ablebody.presentation.home.bookmark.ui.BookmarkListRoute
import com.smilehunter.ablebody.presentation.home.brand.ui.BrandRoute
import com.smilehunter.ablebody.presentation.home.cody.ui.CodyRecommendedRoute
import com.smilehunter.ablebody.presentation.home.item.ui.ItemRoute
import com.smilehunter.ablebody.presentation.main.data.NavigationItems
import com.smilehunter.ablebody.presentation.my.AlarmPage
import com.smilehunter.ablebody.presentation.my.AlarmRoute
import com.smilehunter.ablebody.presentation.my.CouponRegisterRoute
import com.smilehunter.ablebody.presentation.my.CouponRoute
import com.smilehunter.ablebody.presentation.my.MyInfoEditScreenRoute
import com.smilehunter.ablebody.presentation.my.MyInfoScreen
import com.smilehunter.ablebody.presentation.my.MyInfoScreenRoute
import com.smilehunter.ablebody.presentation.my.MyInfomationEditScreen
import com.smilehunter.ablebody.presentation.my.MyProfileRoute
import com.smilehunter.ablebody.presentation.my.NormalUserScreen
import com.smilehunter.ablebody.presentation.my.SettingScreen
import com.smilehunter.ablebody.presentation.my.SuggestPage
import com.smilehunter.ablebody.presentation.my.SuggestRoute
import com.smilehunter.ablebody.presentation.my.WithdrawBeforeScreen
import com.smilehunter.ablebody.presentation.my.WithdrawScreenRoute

const val HomeRoute = "Home"

fun NavGraphBuilder.addHomeGraph(
    isBottomBarShow: (Boolean) -> Unit,
    onSearchBarClick: () -> Unit,
    onAlertButtonClick: () -> Unit,
    onBrandDetailRouteRequest: (Long, String) -> Unit,
    onProductItemDetailRouteRequest: (Long) -> Unit,
    onCodyItemDetailRouteRequest: (Long) -> Unit,
    settingOnClickRouteRequest: () -> Unit,
    onBackRequest: () -> Unit,
    suggestonClick: () -> Unit,
    myInfoOnClick: () -> Unit,
    alarmOnClick: () -> Unit,
    withDrawOnClick: () -> Unit,
    editButtonOnClick: () -> Unit,
    withDrawReasonOnClick: () -> Unit,
    coupononClick: () -> Unit,
    couponRegisterOnClick: () -> Unit
) {
    navigation(
        startDestination = NavigationItems.Brand.name,
        route = "Home",
    ) {
        composable(route = NavigationItems.Brand.name) {
            BrandRoute(
                onSearchBarClick = onSearchBarClick,
                onAlertButtonClick = onAlertButtonClick,
                onItemClick = onBrandDetailRouteRequest,
            )
            isBottomBarShow(true)
        }
        composable(route = NavigationItems.Item.name) {
            ItemRoute(
                onSearchBarClick = onSearchBarClick,
                onAlertButtonClick = onAlertButtonClick,
                itemClick = onProductItemDetailRouteRequest
            )
            isBottomBarShow(true)
        }
        composable(
            route = NavigationItems.CodyRecommendation.name,
        ) {
            CodyRecommendedRoute(
                onSearchBarClick = onSearchBarClick,
                onAlertButtonClick = onAlertButtonClick,
                itemClick = onCodyItemDetailRouteRequest,
            )
            isBottomBarShow(true)
        }
        composable(route = NavigationItems.Bookmark.name) {
            BookmarkListRoute(
                onSearchBarClick = onSearchBarClick,
                onAlertButtonClick = onAlertButtonClick,
                productItemClick = onProductItemDetailRouteRequest,
                codyItemClick = onCodyItemDetailRouteRequest,
            )
            isBottomBarShow(true)
        }
        composable(route = NavigationItems.My.name) {
            MyProfileRoute(
                settingOnClick = settingOnClickRouteRequest,
                coupononClick = coupononClick
            )
            isBottomBarShow(true)
        }
        composable(route = "SettingScreen") {
            SettingScreen(
                onBackRequest = onBackRequest,
                suggestonClick = suggestonClick,
                myInfoOnClick = myInfoOnClick,
                alarmOnClick = alarmOnClick
            )
            isBottomBarShow(false)
        }

        composable(route = "SuggestScreen") {
            SuggestRoute(
                onBackRequest = onBackRequest
            )
            isBottomBarShow(false)
        }


        composable(route = "AlarmScreen") {
            AlarmRoute(
                onBackRequest = onBackRequest
            )
            isBottomBarShow(false)
        }

        composable(route = "WithdrawBeforeScreen") {
            WithdrawBeforeScreen(
                onBackRequest = onBackRequest,
                withDrawReasonOnClick = {withDrawReasonOnClick}
            )
            isBottomBarShow(false)
        }

        composable(route = "MyInfoScreenRoute") {
            MyInfoScreenRoute(
                onBackRequest = onBackRequest,
                withDrawOnClick = withDrawOnClick,
                editButtonOnClick = editButtonOnClick
            )
            isBottomBarShow(false)
        }
        composable(route = "MyInfomationEditScreen") {
            MyInfoEditScreenRoute(
                onBackRequest = onBackRequest
            )
            isBottomBarShow(false)
        }

        composable(route = "WithdrawScreenRoute") {
            WithdrawScreenRoute(
                onBackRequest = onBackRequest
            )
            isBottomBarShow(false)
        }

        composable(route = "CouponRoute") {
            CouponRoute(
                onBackRequest = onBackRequest,
                couponRegisterOnClick = couponRegisterOnClick
            )
            isBottomBarShow(false)
        }

        composable(route = "CouponRegisterRoute") {
            CouponRegisterRoute(
                onBackRequest = onBackRequest
            )
            isBottomBarShow(false)
        }

    }
}