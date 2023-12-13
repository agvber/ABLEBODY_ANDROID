package com.smilehunter.ablebody.presentation.home

import android.util.Log
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.smilehunter.ablebody.presentation.main.data.NavigationItems
import com.smilehunter.ablebody.presentation.my.coupon.ui.CouponRegisterRoute
import com.smilehunter.ablebody.presentation.my.coupon.ui.CouponRoute
import com.smilehunter.ablebody.presentation.my.myInfo.ui.MyInfoEditScreenRoute
import com.smilehunter.ablebody.presentation.my.myInfo.ui.MyInfoScreenRoute
import com.smilehunter.ablebody.presentation.my.myprofile.ui.MyProfileRoute
import com.smilehunter.ablebody.presentation.my.other.ui.OtherNormalUserRoute
import com.smilehunter.ablebody.presentation.my.report.ReportRoute
import com.smilehunter.ablebody.presentation.my.SettingScreen
import com.smilehunter.ablebody.presentation.my.myInfo.ui.WithdrawBeforeScreen
import com.smilehunter.ablebody.presentation.my.alarm.ui.AlarmRoute
import com.smilehunter.ablebody.presentation.my.suggest.ui.SuggestRoute

const val HomeRoute = "Home"

fun NavGraphBuilder.addHomeGraph(
    isBottomBarShow: (Boolean) -> Unit,
    nestedGraph: NavGraphBuilder.() -> Unit,
    settingOnClickRouteRequest: () -> Unit,
    onBackRequest: () -> Unit,
    suggestonClick: () -> Unit,
    myInfoOnClick: () -> Unit,
    alarmOnClick: () -> Unit,
    withDrawOnClick: () -> Unit,
    editButtonOnClick: () -> Unit,
    withDrawReasonOnClick: (String) -> Unit,
    coupononClick: () -> Unit,
    couponRegisterOnClick: () -> Unit,
    onReport: () -> Unit,
    withDrawButtonOnClick: () -> Unit,
    orderManagementOnClick: () -> Unit
) {
    navigation(
        startDestination = NavigationItems.Brand.name,
        route = "Home",
    ) {
        nestedGraph()
        navigation(startDestination = "start", route = NavigationItems.My.name){

            composable(route = "start") {
                MyProfileRoute(
                    settingOnClick = settingOnClickRouteRequest,
                    coupononClick = coupononClick,
                    orderManagementOnClick = orderManagementOnClick
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
//                withDrawReasonOnClick = {Log.d("Home탈퇴 이유",it)}
                    withDrawReasonOnClick = withDrawReasonOnClick
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

//        composable(route = "WithdrawScreenRoute") {
//            WithdrawScreenRoute(
//                onBackRequest = onBackRequest,
//            )
//            isBottomBarShow(false)
//        }

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

//            composable(route = "OtherNormalUserRoute") {
//                OtherNormalUserRoute(
//                    onBackRequest = onBackRequest,
//                    onReport = onReport,
//                    id = "5920702"
//                )
//                isBottomBarShow(true)
//            }

            composable(route = "OtherNormalUserRoute/{uid}",
                arguments = listOf(
                    navArgument("uid") { type = NavType.StringType}
                )
            ){ navBackStackEntry ->
                val uid = navBackStackEntry
                Log.d("다른 유저 프로필homegraph",uid.toString())
                OtherNormalUserRoute(
                    onBackRequest = onBackRequest,
                    onReport = onReport
                )
            }
            isBottomBarShow(true)
        }

            composable(route = "ReportRoute") {
                ReportRoute(
                    onBackRequest = onBackRequest
                )
                isBottomBarShow(false)
            }

        }

}