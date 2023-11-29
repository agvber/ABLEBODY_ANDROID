package com.smilehunter.ablebody.presentation.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.smilehunter.ablebody.presentation.main.data.NavigationItems
import com.smilehunter.ablebody.presentation.my.AlarmRoute
import com.smilehunter.ablebody.presentation.my.CouponRegisterRoute
import com.smilehunter.ablebody.presentation.my.CouponRoute
import com.smilehunter.ablebody.presentation.my.MyInfoEditScreenRoute
import com.smilehunter.ablebody.presentation.my.MyInfoScreenRoute
import com.smilehunter.ablebody.presentation.my.MyProfileRoute
import com.smilehunter.ablebody.presentation.my.OtherNormalUserRoute
import com.smilehunter.ablebody.presentation.my.ReportRoute
import com.smilehunter.ablebody.presentation.my.SettingScreen
import com.smilehunter.ablebody.presentation.my.SuggestRoute
import com.smilehunter.ablebody.presentation.my.WithdrawBeforeScreen

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
    withDrawButtonOnClick: () -> Unit
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

            composable(route = "OtherNormalUserRoute") {
            OtherNormalUserRoute(
                onBackRequest = onBackRequest,
                onReport = onReport,
                id = 5920702
            )
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

}